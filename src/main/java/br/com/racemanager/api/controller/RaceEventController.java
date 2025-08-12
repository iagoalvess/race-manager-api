package br.com.racemanager.api.controller;

import br.com.racemanager.api.dto.RaceEventRequest;
import br.com.racemanager.api.dto.RaceEventResponse;
import br.com.racemanager.api.service.RaceEventService;
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
@RequestMapping("/race-events")
@RequiredArgsConstructor
@Tag(name = "Race Events", description = "Endpoints for managing race events")
public class RaceEventController {
    private final RaceEventService raceEventService;

    @PostMapping
    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "Create race event", description = "Creates a new race event")
    public ResponseEntity<RaceEventResponse> create(
            @Valid @RequestBody RaceEventRequest request) {
        RaceEventResponse response = raceEventService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Operation(summary = "List race events", description = "Lists all race events with pagination")
    public ResponseEntity<Page<RaceEventResponse>> findAll(
            @PageableDefault(size = 20, sort = "eventDate") Pageable pageable) {
        Page<RaceEventResponse> response = raceEventService.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get race event by ID", description = "Returns a specific race event")
    public ResponseEntity<RaceEventResponse> findById(
            @Parameter(description = "Race event ID") @PathVariable Long id) {
        RaceEventResponse response = raceEventService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "Update race event", description = "Updates an existing race event")
    public ResponseEntity<RaceEventResponse> update(
            @Parameter(description = "Race event ID") @PathVariable Long id, 
            @Valid @RequestBody RaceEventRequest request) {
        RaceEventResponse response = raceEventService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "Delete race event", description = "Deletes a race event")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Race event ID") @PathVariable Long id) {
        raceEventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
