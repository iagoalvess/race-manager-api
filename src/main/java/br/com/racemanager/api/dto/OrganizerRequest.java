package br.com.racemanager.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record OrganizerRequest(@NotBlank(message = "The name cannot be blank.") String name,

                               @NotBlank(message = "The email cannot be blank.") @Email String email,

                               @NotBlank(message = "The password cannot be blank.") String password) {
}
