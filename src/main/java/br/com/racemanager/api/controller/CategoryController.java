package br.com.racemanager.api.controller;

import br.com.racemanager.api.dto.CategoryRequest;
import br.com.racemanager.api.dto.CategoryResponse;
import br.com.racemanager.api.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/races/{raceId}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> create(
            @PathVariable Long raceId,
            @RequestBody @Valid CategoryRequest request) {

        CategoryResponse response = categoryService.create(raceId, request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAllByRaceId(@PathVariable Long raceId) {
        List<CategoryResponse> response = categoryService.findAllByRaceId(raceId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> update(
            @PathVariable Long raceId,
            @PathVariable Long categoryId,
            @RequestBody @Valid CategoryRequest request) {

        CategoryResponse response = categoryService.update(raceId, categoryId, request);
        return ResponseEntity.ok(response);
    }
}
