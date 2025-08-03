package br.com.racemanager.api.service;

import br.com.racemanager.api.dto.ParticipantRequest;
import br.com.racemanager.api.dto.ParticipantResponse;
import br.com.racemanager.api.exception.ResourceNotFoundException;
import br.com.racemanager.api.mapper.ParticipantMapper;
import br.com.racemanager.api.model.Category;
import br.com.racemanager.api.model.Participant;
import br.com.racemanager.api.model.RaceEvent;
import br.com.racemanager.api.repository.CategoryRepository;
import br.com.racemanager.api.repository.ParticipantRepository;
import br.com.racemanager.api.repository.RaceEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final CategoryRepository categoryRepository;
    private final RaceEventRepository raceEventRepository;
    private final ParticipantMapper participantMapper;

    public ParticipantResponse create(Long raceId, ParticipantRequest request) {
        RaceEvent raceEvent = raceEventRepository.findById(raceId).orElseThrow(() -> new ResourceNotFoundException("Race not found for id: " + raceId));

        int age = Period.between(request.birthDate(), raceEvent.getEventDate()).getYears();

        Category category = categoryRepository.findCategoryForParticipant(raceId, age, request.gender()).orElseThrow(() -> new ResourceNotFoundException("No category found for age " + age + " and gender " + request.gender()));

        String nextBibNumber = generateNextBibNumber(raceId);

        Participant newParticipant = participantMapper.toEntity(request);
        newParticipant.setRaceEvent(raceEvent);
        newParticipant.setCategory(category);
        newParticipant.setBibNumber(nextBibNumber);

        Participant savedParticipant = participantRepository.save(newParticipant);
        return participantMapper.toResponse(savedParticipant);
    }

    public List<ParticipantResponse> findAllByRaceId(Long raceId) {
        if (!raceEventRepository.existsById(raceId)) {
            throw new ResourceNotFoundException("Race not found for id: " + raceId);
        }
        List<Participant> participants = participantRepository.findByRaceEventId(raceId);
        return participantMapper.toResponse(participants);
    }

    public ParticipantResponse findByRaceIdAndParticipantId(Long raceId, Long participantId) {
        Participant participant = participantRepository.findById(participantId).orElseThrow(() -> new ResourceNotFoundException("Participant not found for id: " + participantId));

        if (!participant.getRaceEvent().getId().equals(raceId)) {
            throw new ResourceNotFoundException("Participant does not belong to the specified race event.");
        }

        return participantMapper.toResponse(participant);
    }

    private String generateNextBibNumber(Long raceId) {
        return participantRepository.findTopByRaceEventIdOrderByBibNumberDesc(raceId).map(lastParticipant -> {
            int lastBib = Integer.parseInt(lastParticipant.getBibNumber());
            return String.valueOf(lastBib + 1);
        }).orElse("1");
    }
}
