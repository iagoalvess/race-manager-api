package br.com.racemanager.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.Duration;

@Entity
@Table(name = "race_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false, unique = true)
    private Participant participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "race_event_id", nullable = false)
    private RaceEvent raceEvent;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "finish_time")
    private LocalDateTime finishTime;

    @Column(name = "total_time_seconds")
    private Long totalTimeSeconds;

    @Column(name = "position")
    private Integer position;

    @Column(name = "category_position")
    private Integer categoryPosition;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RaceStatus status = RaceStatus.REGISTERED;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void calculateTotalTime() {
        if (startTime != null && finishTime != null) {
            Duration duration = Duration.between(startTime, finishTime);
            this.totalTimeSeconds = duration.getSeconds();
        }
    }

    public enum RaceStatus {
        REGISTERED, STARTED, FINISHED, DNF, DNS
    }
}
