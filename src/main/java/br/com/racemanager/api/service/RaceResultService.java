package br.com.racemanager.api.service;

import br.com.racemanager.api.dto.RaceResultRequest;
import br.com.racemanager.api.dto.RaceResultResponse;
import br.com.racemanager.api.exception.BusinessException;
import br.com.racemanager.api.exception.ResourceNotFoundException;
import br.com.racemanager.api.mapper.RaceResultMapper;
import br.com.racemanager.api.model.Participant;
import br.com.racemanager.api.model.RaceEvent;
import br.com.racemanager.api.model.RaceResult;
import br.com.racemanager.api.repository.ParticipantRepository;
import br.com.racemanager.api.repository.RaceEventRepository;
import br.com.racemanager.api.repository.RaceResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RaceResultService {

    private final RaceResultRepository raceResultRepository;
    private final RaceEventRepository raceEventRepository;
    private final ParticipantRepository participantRepository;
    private final RaceResultMapper raceResultMapper;

    @Transactional
    @CacheEvict(value = {"raceResults", "raceResultsByCategory"}, allEntries = true)
    public RaceResultResponse registerFinishTime(Long raceEventId, RaceResultRequest request) {
        RaceEvent raceEvent = raceEventRepository.findById(raceEventId)
                .orElseThrow(() -> new ResourceNotFoundException("Race event not found"));

        if (raceEvent.getStatus() != RaceEvent.RaceStatus.STARTED) {
            throw new BusinessException("Race must be started to register finish times");
        }

        Participant participant = participantRepository.findByBibNumberAndRaceEventId(request.getBibNumber(), raceEventId)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found"));

        RaceResult raceResult = raceResultRepository.findByBibNumberAndRaceEventId(request.getBibNumber(), raceEventId)
                .orElseGet(() -> createInitialRaceResult(participant, raceEvent));

        if (raceResult.getStatus() == RaceResult.RaceStatus.FINISHED) {
            throw new BusinessException("Participant has already finished the race");
        }

        // Parse finish time
        LocalTime finishTime = LocalTime.parse(request.getFinishTime(), DateTimeFormatter.ofPattern("HH:mm:ss"));
        LocalDateTime finishDateTime = LocalDateTime.of(raceEvent.getEventDate(), finishTime);
        
        raceResult.setFinishTime(finishDateTime);
        raceResult.setStatus(RaceResult.RaceStatus.FINISHED);
        raceResult.calculateTotalTime();

        RaceResult savedResult = raceResultRepository.save(raceResult);
        
        // Recalculate positions
        recalculatePositions(raceEventId);
        
        return RaceResultResponse.fromEntity(savedResult);
    }

    @Cacheable(value = "raceResults")
    public Page<RaceResultResponse> getResults(Long raceEventId, Pageable pageable) {
        Page<RaceResult> results = raceResultRepository.findByRaceEventIdOrderByTotalTime(raceEventId, pageable);
        return results.map(RaceResultResponse::fromEntity);
    }

    @Cacheable(value = "raceResultsByCategory")
    public Page<RaceResultResponse> getResultsByCategory(Long raceEventId, Long categoryId, Pageable pageable) {
        Page<RaceResult> results = raceResultRepository.findByRaceEventIdAndCategoryIdOrderByTotalTime(raceEventId, categoryId, pageable);
        return results.map(RaceResultResponse::fromEntity);
    }

    @Transactional
    @CacheEvict(value = {"raceResults", "raceResultsByCategory"}, allEntries = true)
    public void startRace(Long raceEventId) {
        RaceEvent raceEvent = raceEventRepository.findById(raceEventId)
                .orElseThrow(() -> new ResourceNotFoundException("Race event not found"));

        if (raceEvent.getStatus() != RaceEvent.RaceStatus.SCHEDULED) {
            throw new BusinessException("Race must be scheduled to be started");
        }

        raceEvent.setStatus(RaceEvent.RaceStatus.STARTED);
        raceEvent.setActualStartTime(LocalDateTime.now());
        raceEventRepository.save(raceEvent);

        // Create initial race results for all participants
        List<Participant> participants = participantRepository.findByRaceEventId(raceEventId);
        for (Participant participant : participants) {
            RaceResult raceResult = new RaceResult();
            raceResult.setParticipant(participant);
            raceResult.setRaceEvent(raceEvent);
            raceResult.setStartTime(raceEvent.getActualStartTime());
            raceResult.setStatus(RaceResult.RaceStatus.STARTED);
            raceResultRepository.save(raceResult);
        }

        log.info("Race {} started with {} participants", raceEventId, participants.size());
    }

    private RaceResult createInitialRaceResult(Participant participant, RaceEvent raceEvent) {
        RaceResult raceResult = new RaceResult();
        raceResult.setParticipant(participant);
        raceResult.setRaceEvent(raceEvent);
        raceResult.setStartTime(raceEvent.getActualStartTime());
        raceResult.setStatus(RaceResult.RaceStatus.STARTED);
        return raceResultRepository.save(raceResult);
    }

    private void recalculatePositions(Long raceEventId) {
        List<RaceResult> finishedResults = raceResultRepository.findByRaceEventIdAndStatus(raceEventId, RaceResult.RaceStatus.FINISHED);
        
        // Sort by total time
        finishedResults.sort((r1, r2) -> {
            if (r1.getTotalTimeSeconds() == null) return 1;
            if (r2.getTotalTimeSeconds() == null) return -1;
            return r1.getTotalTimeSeconds().compareTo(r2.getTotalTimeSeconds());
        });

        // Set general positions
        for (int i = 0; i < finishedResults.size(); i++) {
            finishedResults.get(i).setPosition(i + 1);
        }

        // Set category positions
        finishedResults.stream()
                .collect(java.util.stream.Collectors.groupingBy(r -> r.getParticipant().getCategory().getId()))
                .forEach((categoryId, categoryResults) -> {
                    categoryResults.sort((r1, r2) -> {
                        if (r1.getTotalTimeSeconds() == null) return 1;
                        if (r2.getTotalTimeSeconds() == null) return -1;
                        return r1.getTotalTimeSeconds().compareTo(r2.getTotalTimeSeconds());
                    });
                    
                    for (int i = 0; i < categoryResults.size(); i++) {
                        categoryResults.get(i).setCategoryPosition(i + 1);
                    }
                });

        raceResultRepository.saveAll(finishedResults);
    }
}
