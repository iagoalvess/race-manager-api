package br.com.racemanager.api.mapper;

import br.com.racemanager.api.dto.ParticipantRequest;
import br.com.racemanager.api.dto.ParticipantResponse;
import br.com.racemanager.api.model.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {
    Participant toEntity(ParticipantRequest request);

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "raceEvent.id", target = "raceEventId")
    ParticipantResponse toResponse(Participant participant);

    List<ParticipantResponse> toResponse(List<Participant> participants);
}
