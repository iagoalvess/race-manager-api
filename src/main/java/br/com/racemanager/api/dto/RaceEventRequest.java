package br.com.racemanager.api.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record RaceEventRequest(
        @NotBlank(message = "The name cannot be blank.")
        String name,

        @NotNull(message = "The event date cannot be null")
        @Future(message = "The event date must be in the future")
        LocalDate eventDate,

        @NotBlank(message = "Location cannot be blank")
        String location,

        @NotNull(message = "Distance cannot be null")
        @Positive(message = "Distance must be a positive value")
        Double distanceInKm
) {
}
