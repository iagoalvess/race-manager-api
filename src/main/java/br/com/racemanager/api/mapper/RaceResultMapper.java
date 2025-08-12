package br.com.racemanager.api.mapper;

import br.com.racemanager.api.dto.RaceResultResponse;
import br.com.racemanager.api.model.RaceResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RaceResultMapper {

    @Mapping(target = "participantName", source = "participant.fullName")
    @Mapping(target = "bibNumber", source = "participant.bibNumber")
    @Mapping(target = "categoryName", source = "participant.category.name")
    RaceResultResponse toResponse(RaceResult raceResult);

    List<RaceResultResponse> toResponseList(List<RaceResult> raceResults);
}
