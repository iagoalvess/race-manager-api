package br.com.racemanager.api.controller;

import br.com.racemanager.api.dto.RaceResultRequest;
import br.com.racemanager.api.dto.RaceResultResponse;
import br.com.racemanager.api.service.RaceResultService;
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

@RestController
@RequestMapping("/race-events/{raceEventId}")
@RequiredArgsConstructor
@Tag(name = "Race Results", description = "Endpoints for managing race results")
public class RaceResultController {

    private final RaceResultService raceResultService;

    @PostMapping("/start")
    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "Start race", description = "Starts a race and records the start time")
    public ResponseEntity<Void> startRace(
            @Parameter(description = "Race event ID") @PathVariable Long raceEventId) {
        raceResultService.startRace(raceEventId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/finish")
    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "Register finish time", description = "Registers the finish time of a participant")
    public ResponseEntity<RaceResultResponse> registerFinishTime(
            @Parameter(description = "Race event ID") @PathVariable Long raceEventId,
            @Valid @RequestBody RaceResultRequest request) {
        return ResponseEntity.ok(raceResultService.registerFinishTime(raceEventId, request));
    }

    @GetMapping("/results")
    @Operation(summary = "List results", description = "Lists the race results with pagination")
    public ResponseEntity<Page<RaceResultResponse>> getResults(
            @Parameter(description = "Race event ID") @PathVariable Long raceEventId,
            @PageableDefault(size = 20, sort = "position") Pageable pageable) {
        return ResponseEntity.ok(raceResultService.getResults(raceEventId, pageable));
    }

    @GetMapping("/results/category/{categoryId}")
    @Operation(summary = "List results by category", description = "Lists the race results by category with pagination")
    public ResponseEntity<Page<RaceResultResponse>> getResultsByCategory(
            @Parameter(description = "Race event ID") @PathVariable Long raceEventId,
            @Parameter(description = "Category ID") @PathVariable Long categoryId,
            @PageableDefault(size = 20, sort = "categoryPosition") Pageable pageable) {
        return ResponseEntity.ok(raceResultService.getResultsByCategory(raceEventId, categoryId, pageable));
    }
}
