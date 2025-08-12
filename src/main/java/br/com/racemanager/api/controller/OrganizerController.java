package br.com.racemanager.api.controller;

import br.com.racemanager.api.dto.OrganizerRequest;
import br.com.racemanager.api.dto.OrganizerResponse;
import br.com.racemanager.api.service.OrganizerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/organizers")
@RequiredArgsConstructor
@Tag(name = "Organizers", description = "Endpoints for managing organizers")
public class OrganizerController {
    private final OrganizerService organizerService;

    @PostMapping
    @Operation(summary = "Create organizer", description = "Creates a new organizer")
    public ResponseEntity<OrganizerResponse> create(@Valid @RequestBody OrganizerRequest request) {
        OrganizerResponse response = organizerService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "List organizers", description = "Lists all organizers")
    public ResponseEntity<List<OrganizerResponse>> findAll() {
        return ResponseEntity.ok(organizerService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "Get organizer", description = "Returns a specific organizer")
    public ResponseEntity<OrganizerResponse> findById(
            @Parameter(description = "Organizer ID") @PathVariable Long id) {
        return ResponseEntity.ok(organizerService.findById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "Delete organizer", description = "Deletes an organizer")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Organizer ID") @PathVariable Long id) {
        organizerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
