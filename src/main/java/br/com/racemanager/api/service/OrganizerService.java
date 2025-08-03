package br.com.racemanager.api.service;

import br.com.racemanager.api.dto.OrganizerRequest;
import br.com.racemanager.api.dto.OrganizerResponse;
import br.com.racemanager.api.exception.BusinessException;
import br.com.racemanager.api.exception.ResourceNotFoundException;
import br.com.racemanager.api.mapper.OrganizerMapper;
import br.com.racemanager.api.model.Organizer;
import br.com.racemanager.api.repository.OrganizerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizerService {
    private final OrganizerRepository organizerRepository;
    private final OrganizerMapper organizerMapper;

    public OrganizerResponse create(OrganizerRequest request) {
        if (organizerRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email already exists");
        }

        Organizer newOrganizer = organizerMapper.toEntity(request);
        Organizer savedOrganizer = organizerRepository.save(newOrganizer);

        return organizerMapper.toResponse(savedOrganizer);
    }

    public List<OrganizerResponse> findAll() {
        return organizerMapper.toResponse(organizerRepository.findAll());
    }

    public OrganizerResponse findById(Long id) {
        Organizer organizer = organizerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Organizer not found for id: " + id));

        return organizerMapper.toResponse(organizer);
    }

    public void delete(Long id) {
        if (!organizerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Organizer not found for id: " + id);
        }

        organizerRepository.deleteById(id);
    }
}
