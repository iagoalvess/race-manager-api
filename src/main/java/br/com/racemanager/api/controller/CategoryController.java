package br.com.racemanager.api.controller;

import br.com.racemanager.api.dto.CategoryRequest;
import br.com.racemanager.api.dto.CategoryResponse;
import br.com.racemanager.api.service.CategoryService;
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
@RequestMapping("/race-events/{raceEventId}/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Endpoints for managing categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "Create category", description = "Creates a new category for the race")
    public ResponseEntity<CategoryResponse> create(
            @Parameter(description = "Race event ID") @PathVariable Long raceEventId,
            @Valid @RequestBody CategoryRequest request) {

        CategoryResponse response = categoryService.create(raceEventId, request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Operation(summary = "List categories", description = "Lists all categories of the race")
    public ResponseEntity<List<CategoryResponse>> findAllByRaceId(
            @Parameter(description = "Race event ID") @PathVariable Long raceEventId) {
        List<CategoryResponse> response = categoryService.findAllByRaceId(raceEventId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "Update category", description = "Updates an existing category")
    public ResponseEntity<CategoryResponse> update(
            @Parameter(description = "Race event ID") @PathVariable Long raceEventId,
            @Parameter(description = "Category ID") @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequest request) {

        CategoryResponse response = categoryService.update(raceEventId, categoryId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "Delete category", description = "Deletes a category")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Race event ID") @PathVariable Long raceEventId,
            @Parameter(description = "Category ID") @PathVariable Long categoryId) {
        categoryService.delete(raceEventId, categoryId);
        return ResponseEntity.noContent().build();
    }
}
