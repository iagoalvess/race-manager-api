package br.com.racemanager.api.repository;

import br.com.racemanager.api.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findTopByRaceEventIdOrderByBibNumberDesc(Long raceEventId);

    List<Participant> findByRaceEventId(Long raceEventId);

    boolean existsByCpf(String cpf);
}
