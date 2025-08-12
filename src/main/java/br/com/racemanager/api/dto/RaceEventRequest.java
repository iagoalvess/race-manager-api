package br.com.racemanager.api.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaceEventRequest {

    @NotBlank(message = "Name cannot be blank.")
    private String name;

    @NotNull(message = "Event date cannot be null")
    @Future(message = "Event date must be in the future")
    private LocalDate eventDate;

    @NotBlank(message = "Location cannot be blank")
    private String location;

    @NotNull(message = "Distance cannot be null")
    @Positive(message = "Distance must be a positive value")
    private Double distanceInKm;
}
