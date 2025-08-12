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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RaceEventService {
    private final RaceEventRepository raceEventRepository;
    private final RaceEventMapper raceEventMapper;
    private final OrganizerRepository organizerRepository;

    @CacheEvict(value = "raceEvents", allEntries = true)
    public RaceEventResponse create(RaceEventRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String organizerEmail = authentication.getName();
        
        Organizer organizer = organizerRepository.findByEmail(organizerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Organizer not found for email: " + organizerEmail));

        RaceEvent newRaceEvent = raceEventMapper.toEntity(request);
        newRaceEvent.setOrganizer(organizer);

        RaceEvent savedRaceEvent = raceEventRepository.save(newRaceEvent);
        return raceEventMapper.toResponse(savedRaceEvent);
    }

    @Cacheable(value = "raceEvents")
    public Page<RaceEventResponse> findAll(Pageable pageable) {
        Page<RaceEvent> events = raceEventRepository.findAll(pageable);
        return events.map(raceEventMapper::toResponse);
    }

    @Cacheable(value = "raceEvents", key = "#id")
    public RaceEventResponse findById(Long id) {
        RaceEvent event = raceEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Race event not found for ID: " + id));
        return raceEventMapper.toResponse(event);
    }

    @CacheEvict(value = "raceEvents", allEntries = true)
    public RaceEventResponse update(Long id, RaceEventRequest request) {
        RaceEvent eventToUpdate = raceEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Race event not found for ID: " + id));

        raceEventMapper.updateEntityFromRequest(request, eventToUpdate);
        RaceEvent updatedEvent = raceEventRepository.save(eventToUpdate);
        return raceEventMapper.toResponse(updatedEvent);
    }

    @CacheEvict(value = "raceEvents", allEntries = true)
    public void delete(Long id) {
        raceEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Race event not found for ID: " + id));
        raceEventRepository.deleteById(id);
    }
}
