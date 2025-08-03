package br.com.racemanager.api.controller;

import br.com.racemanager.api.dto.ParticipantRequest;
import br.com.racemanager.api.dto.ParticipantResponse;
import br.com.racemanager.api.service.ParticipantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/races/{raceId}/participants")
@RequiredArgsConstructor
public class ParticipantController {
    private final ParticipantService participantService;

    @PostMapping
    public ResponseEntity<ParticipantResponse> create(@PathVariable Long raceId, @RequestBody @Valid ParticipantRequest request) {

        ParticipantResponse response = participantService.create(raceId, request);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/races/{raceId}/participants/{participantId}").buildAndExpand(raceId, response.id()).toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ParticipantResponse>> findAllByRaceId(@PathVariable Long raceId) {
        List<ParticipantResponse> response = participantService.findAllByRaceId(raceId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{participantId}")
    public ResponseEntity<ParticipantResponse> findById(@PathVariable Long raceId, @PathVariable Long participantId) {
        ParticipantResponse response = participantService.findByRaceIdAndParticipantId(raceId, participantId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{participantId}")
    public ResponseEntity<Void> delete(@PathVariable Long raceId, @PathVariable Long participantId) {

        participantService.delete(raceId, participantId);
        return ResponseEntity.noContent().build();
    }
}
