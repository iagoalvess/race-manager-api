package br.com.racemanager.api.dto;

import java.time.LocalDate;

public record ParticipantResponse(
        Long id,
        String fullName,
        String bibNumber,
        String gender,
        LocalDate birthDate,
        String categoryName,
        Long raceEventId
) {
}
