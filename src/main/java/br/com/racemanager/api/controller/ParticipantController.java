package br.com.racemanager.api.controller;

import br.com.racemanager.api.dto.ParticipantRequest;
import br.com.racemanager.api.dto.ParticipantResponse;
import br.com.racemanager.api.service.ParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/race-events/{raceEventId}/participants")
@RequiredArgsConstructor
@Tag(name = "Participants", description = "Endpoints for managing participants")
public class ParticipantController {
    private final ParticipantService participantService;

    @PostMapping
    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "Register participant", description = "Registers a new participant in the race")
    public ResponseEntity<ParticipantResponse> create(
            @Parameter(description = "Race event ID") @PathVariable Long raceEventId, 
            @Valid @RequestBody ParticipantRequest request) {
        ParticipantResponse response = participantService.create(raceEventId, request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{participantId}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Operation(summary = "List participants", description = "Lists all participants of the race with pagination")
    public ResponseEntity<Page<ParticipantResponse>> findAllByRaceId(
            @Parameter(description = "Race event ID") @PathVariable Long raceEventId,
            @PageableDefault(size = 20, sort = "bibNumber") Pageable pageable) {
        Page<ParticipantResponse> response = participantService.findAllByRaceId(raceEventId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{participantId}")
    @Operation(summary = "Get participant", description = "Returns a specific participant")
    public ResponseEntity<ParticipantResponse> findById(
            @Parameter(description = "Race event ID") @PathVariable Long raceEventId, 
            @Parameter(description = "Participant ID") @PathVariable Long participantId) {
        ParticipantResponse response = participantService.findByRaceIdAndParticipantId(raceEventId, participantId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{participantId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "Delete participant", description = "Removes a participant from the race")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Race event ID") @PathVariable Long raceEventId, 
            @Parameter(description = "Participant ID") @PathVariable Long participantId) {
        participantService.delete(raceEventId, participantId);
        return ResponseEntity.noContent().build();
    }
}
