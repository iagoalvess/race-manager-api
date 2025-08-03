package br.com.racemanager.api.service;

import br.com.racemanager.api.dto.RaceEventRequest;
import br.com.racemanager.api.dto.RaceEventResponse;
import br.com.racemanager.api.exception.ResourceNotFoundException;
import br.com.racemanager.api.mapper.RaceEventMapper;
import br.com.racemanager.api.model.Organizer;
import br.com.racemanager.api.model.RaceEvent;
import br.com.racemanager.api.repository.OrganizerRepository;
import br.com.racemanager.api.repository.RaceEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RaceEventService {
    private final RaceEventRepository raceEventRepository;
    private final RaceEventMapper raceEventMapper;
    private final OrganizerRepository organizerRepository;

    public RaceEventResponse create(Long organizerId, RaceEventRequest request) {
        Organizer organizer = organizerRepository.findById(organizerId).orElseThrow(() -> new ResourceNotFoundException("Organizer not found for ID: " + organizerId));

        RaceEvent newRaceEvent = raceEventMapper.toEntity(request);

        newRaceEvent.setOrganizer(organizer);

        RaceEvent savedRaceEvent = raceEventRepository.save(newRaceEvent);

        return raceEventMapper.toResponse(savedRaceEvent);
    }

    public List<RaceEventResponse> findAll() {
        List<RaceEvent> events = raceEventRepository.findAll();

        return raceEventMapper.toResponse(events);
    }

    public RaceEventResponse findById(Long id) {
        RaceEvent event = raceEventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Race event not found for ID: " + id));

        return raceEventMapper.toResponse(event);
    }

    public RaceEventResponse update(Long id, RaceEventRequest request) {
        RaceEvent eventToUpdate = raceEventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Race event not found for ID: " + id));

        raceEventMapper.updateEntityFromRequest(request, eventToUpdate);

        RaceEvent updatedEvent = raceEventRepository.save(eventToUpdate);

        return raceEventMapper.toResponse(updatedEvent);
    }

    public void delete(Long id) {
        raceEventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Race event not found for ID: " + id));

        raceEventRepository.deleteById(id);
    }
}
