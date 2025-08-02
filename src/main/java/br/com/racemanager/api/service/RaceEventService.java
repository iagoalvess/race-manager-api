package br.com.racemanager.api.service;

import br.com.racemanager.api.dto.RaceEventRequest;
import br.com.racemanager.api.dto.RaceEventResponse;
import br.com.racemanager.api.model.RaceEvent;
import br.com.racemanager.api.repository.RaceEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RaceEventService {
    private final RaceEventRepository raceEventRepository;

    public RaceEventResponse create(RaceEventRequest request) {
        RaceEvent newRaceEvent = new RaceEvent();
        newRaceEvent.setName(request.name());
        newRaceEvent.setEventDate(request.eventDate());
        newRaceEvent.setLocation(request.location());
        newRaceEvent.setDistanceInKm(request.distanceInKm());

        RaceEvent savedRaceEvent = raceEventRepository.save(newRaceEvent);

        return new RaceEventResponse(
                savedRaceEvent.getId(),
                savedRaceEvent.getName(),
                savedRaceEvent.getEventDate(),
                savedRaceEvent.getLocation(),
                savedRaceEvent.getDistanceInKm()
        );
    }
}
