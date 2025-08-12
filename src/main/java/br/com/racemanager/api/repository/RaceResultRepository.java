package br.com.racemanager.api.repository;

import br.com.racemanager.api.model.RaceResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RaceResultRepository extends JpaRepository<RaceResult, Long> {

    @Query("SELECT rr FROM RaceResult rr WHERE rr.raceEvent.id = :raceEventId ORDER BY rr.totalTimeSeconds ASC NULLS LAST")
    Page<RaceResult> findByRaceEventIdOrderByTotalTime(@Param("raceEventId") Long raceEventId, Pageable pageable);

    @Query("SELECT rr FROM RaceResult rr WHERE rr.raceEvent.id = :raceEventId AND rr.participant.category.id = :categoryId ORDER BY rr.totalTimeSeconds ASC NULLS LAST")
    Page<RaceResult> findByRaceEventIdAndCategoryIdOrderByTotalTime(@Param("raceEventId") Long raceEventId, @Param("categoryId") Long categoryId, Pageable pageable);

    @Query("SELECT rr FROM RaceResult rr WHERE rr.participant.bibNumber = :bibNumber AND rr.raceEvent.id = :raceEventId")
    Optional<RaceResult> findByBibNumberAndRaceEventId(@Param("bibNumber") String bibNumber, @Param("raceEventId") Long raceEventId);

    @Query("SELECT rr FROM RaceResult rr WHERE rr.raceEvent.id = :raceEventId AND rr.status = :status")
    List<RaceResult> findByRaceEventIdAndStatus(@Param("raceEventId") Long raceEventId, @Param("status") RaceResult.RaceStatus status);

    @Query("SELECT COUNT(rr) FROM RaceResult rr WHERE rr.raceEvent.id = :raceEventId AND rr.status = 'FINISHED'")
    Long countFinishedParticipantsByRaceEventId(@Param("raceEventId") Long raceEventId);

    @Query("SELECT rr FROM RaceResult rr WHERE rr.raceEvent.id = :raceEventId ORDER BY rr.position ASC")
    List<RaceResult> findTopResultsByRaceEventId(@Param("raceEventId") Long raceEventId, Pageable pageable);
}
