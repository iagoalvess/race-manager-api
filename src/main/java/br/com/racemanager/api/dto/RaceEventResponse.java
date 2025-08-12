package br.com.racemanager.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaceEventResponse {
    private Long id;
    private String name;
    private LocalDate eventDate;
    private String location;
    private Double distanceInKm;
}
