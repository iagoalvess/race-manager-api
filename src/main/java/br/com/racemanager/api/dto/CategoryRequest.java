package br.com.racemanager.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "Name cannot be blank.")
    private String name;

    @NotNull(message = "Minimum age cannot be null")
    @PositiveOrZero
    private Integer minAge;

    @NotNull(message = "Maximum age cannot be null")
    @PositiveOrZero
    private Integer maxAge;

    @NotBlank(message = "Gender cannot be blank")
    private String gender;
}
