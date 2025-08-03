package br.com.racemanager.api.dto;

public record CategoryResponse(
        Long id,
        String name,
        int minAge,
        int maxAge,
        String gender,
        Long raceEventId
) {
}
