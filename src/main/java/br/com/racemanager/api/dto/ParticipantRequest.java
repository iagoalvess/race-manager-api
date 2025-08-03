package br.com.racemanager.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record ParticipantRequest(
        @NotBlank(message = "The name cannot be blank.")
        String fullName,

        @NotBlank(message = "The cpf cannot be blank.")
        String cpf,

        @NotNull(message = "The gender cannot be null")
        String gender,

        @NotNull(message = "The birth date cannot be null")
        @Past(message = "The birth date must be in the past")
        LocalDate birthDate
) {
}
