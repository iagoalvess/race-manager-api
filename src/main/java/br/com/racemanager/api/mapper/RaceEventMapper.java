package br.com.racemanager.api.mapper;

import br.com.racemanager.api.dto.RaceEventResponse;
import br.com.racemanager.api.model.RaceEvent;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RaceEventMapper {
    RaceEventResponse toResponse(RaceEvent raceEvent);

    List<RaceEventResponse> toResponse(List<RaceEvent> events);
}
