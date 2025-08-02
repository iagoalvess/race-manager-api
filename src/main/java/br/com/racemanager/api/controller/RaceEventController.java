package br.com.racemanager.api.controller;

import br.com.racemanager.api.dto.RaceEventRequest;
import br.com.racemanager.api.dto.RaceEventResponse;
import br.com.racemanager.api.service.RaceEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/races")
@RequiredArgsConstructor
public class RaceEventController {
    private final RaceEventService raceEventService;

    @PostMapping
    public ResponseEntity<RaceEventResponse> create(@RequestBody @Valid RaceEventRequest request) {
        RaceEventResponse response = raceEventService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<RaceEventResponse>> findAll() {
        List<RaceEventResponse> response = raceEventService.findAll();

        return ResponseEntity.ok(response);
    }
}
