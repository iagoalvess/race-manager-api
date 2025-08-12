package br.com.racemanager.api.repository;

import br.com.racemanager.api.model.Participant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findTopByRaceEventIdOrderByBibNumberDesc(Long raceEventId);

    List<Participant> findByRaceEventId(Long raceEventId);
    
    Page<Participant> findByRaceEventId(Long raceEventId, Pageable pageable);

    boolean existsByCpf(String cpf);

    @Query("SELECT p FROM Participant p WHERE p.bibNumber = :bibNumber AND p.raceEvent.id = :raceEventId")
    Optional<Participant> findByBibNumberAndRaceEventId(@Param("bibNumber") String bibNumber, @Param("raceEventId") Long raceEventId);
}
