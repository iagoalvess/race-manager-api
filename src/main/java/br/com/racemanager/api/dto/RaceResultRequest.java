package br.com.racemanager.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceResultRequest {

    @NotBlank(message = "Bib number is required")
    private String bibNumber;

    @NotNull(message = "Finish time is required")
    private String finishTime;
}
