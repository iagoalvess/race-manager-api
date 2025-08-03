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
}
