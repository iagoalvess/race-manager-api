package br.com.racemanager.api.controller;

import br.com.racemanager.api.dto.OrganizerRequest;
import br.com.racemanager.api.dto.OrganizerResponse;
import br.com.racemanager.api.service.OrganizerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/organizers")
@RequiredArgsConstructor
public class OrganizerController {
    private final OrganizerService organizerService;

    @PostMapping
    public ResponseEntity<OrganizerResponse> create(@RequestBody @Valid OrganizerRequest request) {
        OrganizerResponse response = organizerService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrganizerResponse>> findAll() {
        return ResponseEntity.ok(organizerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizerResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(organizerService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        organizerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
