package br.com.racemanager.api.repository;

import br.com.racemanager.api.model.RaceEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RaceEventRepository extends JpaRepository<RaceEvent, Long> {

}
