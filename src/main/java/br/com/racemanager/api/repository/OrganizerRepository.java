package br.com.racemanager.api.repository;

import br.com.racemanager.api.model.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizerRepository extends JpaRepository<Organizer, Long> {
    boolean existsByEmail(String email);
}
