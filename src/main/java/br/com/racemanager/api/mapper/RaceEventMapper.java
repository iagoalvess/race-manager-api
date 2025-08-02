package br.com.racemanager.api.mapper;

import br.com.racemanager.api.dto.RaceEventRequest;
import br.com.racemanager.api.dto.RaceEventResponse;
import br.com.racemanager.api.model.RaceEvent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RaceEventMapper {
    RaceEvent toEntity(RaceEventRequest request);

    RaceEventResponse toResponse(RaceEvent event);

    List<RaceEventResponse> toResponse(List<RaceEvent> events);

    void updateEntityFromRequest(RaceEventRequest request, @MappingTarget RaceEvent entity);
}
