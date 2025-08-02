package br.com.racemanager.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "race_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate eventDate;

    private String location;

    private Double distanceInKm;

    private LocalTime startTime;
}
