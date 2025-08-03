package br.com.racemanager.api.mapper;

import br.com.racemanager.api.dto.OrganizerRequest;
import br.com.racemanager.api.dto.OrganizerResponse;
import br.com.racemanager.api.model.Organizer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrganizerMapper {
    Organizer toEntity(OrganizerRequest request);

    OrganizerResponse toResponse(Organizer organizer);

    List<OrganizerResponse> toResponse(List<Organizer> organizers);
}
