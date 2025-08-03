package br.com.racemanager.api.service;

import br.com.racemanager.api.dto.CategoryRequest;
import br.com.racemanager.api.dto.CategoryResponse;
import br.com.racemanager.api.exception.ResourceNotFoundException;
import br.com.racemanager.api.mapper.CategoryMapper;
import br.com.racemanager.api.model.Category;
import br.com.racemanager.api.model.RaceEvent;
import br.com.racemanager.api.repository.CategoryRepository;
import br.com.racemanager.api.repository.RaceEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final RaceEventRepository raceEventRepository;
    private final CategoryMapper categoryMapper;

    public CategoryResponse create(Long raceId, CategoryRequest request) {
        RaceEvent raceEvent = raceEventRepository.findById(raceId)
                .orElseThrow(() -> new ResourceNotFoundException("Race not found for id: " + raceId));

        Category newCategory = categoryMapper.toEntity(request);
        newCategory.setRaceEvent(raceEvent);

        Category savedCategory = categoryRepository.save(newCategory);
        return categoryMapper.toResponse(savedCategory);
    }

    public List<CategoryResponse> findAllByRaceId(Long raceId) {
        if (!raceEventRepository.existsById(raceId)) {
            throw new ResourceNotFoundException("Race not found for id: " + raceId);
        }
        List<Category> categories = categoryRepository.findByRaceEventId(raceId);
        return categoryMapper.toResponse(categories);
    }
}
