package br.com.racemanager.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CategoryRequest(
        @NotBlank String name,
        @NotNull @PositiveOrZero int minAge,
        @NotNull @PositiveOrZero int maxAge,
        @NotBlank String gender
) {
}
