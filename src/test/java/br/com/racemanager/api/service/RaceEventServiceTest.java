package br.com.racemanager.api.service;

import br.com.racemanager.api.dto.RaceEventRequest;
import br.com.racemanager.api.dto.RaceEventResponse;
import br.com.racemanager.api.mapper.RaceEventMapper;
import br.com.racemanager.api.model.RaceEvent;
import br.com.racemanager.api.repository.RaceEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RaceEventServiceTest {

    @Mock
    private RaceEventRepository raceEventRepository;

    @Mock
    private RaceEventMapper raceEventMapper;

    @InjectMocks
    private RaceEventService raceEventService;

    @Test
    void shouldCreateRaceEventSuccessfully() {
        var request = RaceEventRequest.builder()
                .name("Test Race")
                .eventDate(LocalDate.now().plusDays(10))
                .location("Test Location")
                .distanceInKm(5.0)
                .build();

        var raceEvent = new RaceEvent();
        var savedRaceEvent = new RaceEvent();
        savedRaceEvent.setId(1L);

        var expectedResponse = RaceEventResponse.builder()
                .id(1L)
                .name("Test Race")
                .eventDate(LocalDate.now().plusDays(10))
                .location("Test Location")
                .distanceInKm(5.0)
                .build();

        when(raceEventMapper.toEntity(any(RaceEventRequest.class))).thenReturn(raceEvent);
        when(raceEventRepository.save(any(RaceEvent.class))).thenReturn(savedRaceEvent);
        when(raceEventMapper.toResponse(any(RaceEvent.class))).thenReturn(expectedResponse);

        RaceEventResponse actualResponse = raceEventService.create(request);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getId()).isEqualTo(1L);
        assertThat(actualResponse.getName()).isEqualTo("Test Race");
    }
}