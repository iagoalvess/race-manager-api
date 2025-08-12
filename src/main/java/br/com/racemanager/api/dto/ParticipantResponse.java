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
public class ParticipantResponse {
    private Long id;
    private String fullName;
    private String cpf;
    private String bibNumber;
    private String gender;
    private LocalDate birthDate;
    private String categoryName;
    private Long raceEventId;
}
