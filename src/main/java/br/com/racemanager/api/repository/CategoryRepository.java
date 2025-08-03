package br.com.racemanager.api.repository;

import br.com.racemanager.api.model.Category;
import br.com.racemanager.api.model.RaceEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByRaceEventId(Long raceId);

    @Query("SELECT c FROM Category c WHERE c.raceEvent.id = :raceId AND c.gender = :gender AND :age BETWEEN c.minAge AND c.maxAge")
    Optional<Category> findCategoryForParticipant(Long raceId, int age, String gender);
}
