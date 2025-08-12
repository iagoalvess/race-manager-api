package br.com.racemanager.api.service;

import br.com.racemanager.api.dto.ParticipantRequest;
import br.com.racemanager.api.dto.ParticipantResponse;
import br.com.racemanager.api.exception.BusinessException;
import br.com.racemanager.api.exception.ResourceNotFoundException;
import br.com.racemanager.api.mapper.ParticipantMapper;
import br.com.racemanager.api.model.Category;
import br.com.racemanager.api.model.Participant;
import br.com.racemanager.api.model.RaceEvent;
import br.com.racemanager.api.repository.CategoryRepository;
import br.com.racemanager.api.repository.ParticipantRepository;
import br.com.racemanager.api.repository.RaceEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Period;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final CategoryRepository categoryRepository;
    private final RaceEventRepository raceEventRepository;
    private final ParticipantMapper participantMapper;

    @CacheEvict(value = "participants", allEntries = true)
    public ParticipantResponse create(Long raceEventId, ParticipantRequest request) {
        if (participantRepository.existsByCpf(request.getCpf())) {
            throw new BusinessException("CPF already registered in the system.");
        }

        RaceEvent raceEvent = raceEventRepository.findById(raceEventId)
                .orElseThrow(() -> new ResourceNotFoundException("Race event not found for ID: " + raceEventId));

        int age = Period.between(request.getBirthDate(), raceEvent.getEventDate()).getYears();

        Category category = categoryRepository.findCategoryForParticipant(raceEventId, age, request.getGender())
                .orElseThrow(() -> new ResourceNotFoundException("No category found for age " + age + " and gender " + request.getGender()));

        String nextBibNumber = generateNextBibNumber(raceEventId);

        Participant newParticipant = participantMapper.toEntity(request);
        newParticipant.setRaceEvent(raceEvent);
        newParticipant.setCategory(category);
        newParticipant.setBibNumber(nextBibNumber);

        Participant savedParticipant = participantRepository.save(newParticipant);
        return participantMapper.toResponse(savedParticipant);
    }

    @Cacheable(value = "participants", key = "#raceEventId")
    public Page<ParticipantResponse> findAllByRaceId(Long raceEventId, Pageable pageable) {
        if (!raceEventRepository.existsById(raceEventId)) {
            throw new ResourceNotFoundException("Race event not found for ID: " + raceEventId);
        }
        Page<Participant> participants = participantRepository.findByRaceEventId(raceEventId, pageable);
        return participants.map(participantMapper::toResponse);
    }

    @Cacheable(value = "participants", key = "#raceEventId + '_' + #participantId")
    public ParticipantResponse findByRaceIdAndParticipantId(Long raceEventId, Long participantId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found for ID: " + participantId));

        if (!participant.getRaceEvent().getId().equals(raceEventId)) {
            throw new ResourceNotFoundException("Participant does not belong to the specified race event.");
        }

        return participantMapper.toResponse(participant);
    }

    @CacheEvict(value = "participants", allEntries = true)
    public void delete(Long raceEventId, Long participantId) {
        Participant participantToDelete = participantRepository.findById(participantId)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found for ID: " + participantId));

        if (!participantToDelete.getRaceEvent().getId().equals(raceEventId)) {
            throw new ResourceNotFoundException("Participant does not belong to the specified race event.");
        }

        participantRepository.delete(participantToDelete);
    }

    private String generateNextBibNumber(Long raceEventId) {
        return participantRepository.findTopByRaceEventIdOrderByBibNumberDesc(raceEventId)
                .map(lastParticipant -> {
                    int lastBib = Integer.parseInt(lastParticipant.getBibNumber());
                    return String.valueOf(lastBib + 1);
                }).orElse("1");
    }
}
