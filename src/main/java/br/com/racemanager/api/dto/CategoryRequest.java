package br.com.racemanager.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CategoryRequest(
        @NotBlank(message = "The name cannot be blank.")
        String name,

        @NotNull(message = "The min age cannot be null")
        @PositiveOrZero
        int minAge,

        @NotNull(message = "The max age cannot be null")
        @PositiveOrZero
        int maxAge,

        @NotBlank(message = "The gender cannot be null")
        String gender
) {
}
