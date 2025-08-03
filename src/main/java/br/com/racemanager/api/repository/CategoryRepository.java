package br.com.racemanager.api.repository;

import br.com.racemanager.api.model.Category;
import br.com.racemanager.api.model.RaceEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByRaceEventId(Long raceId);
}
