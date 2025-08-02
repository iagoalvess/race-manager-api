package br.com.racemanager.api.dto;

import java.time.LocalDate;

public record RaceEventResponse (
    Long id,
    String name,
    LocalDate eventDate,
    String location,
    Double distanceInKm
) {
}
