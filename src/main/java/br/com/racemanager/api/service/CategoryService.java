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

    public CategoryResponse create(Long raceEventId, CategoryRequest request) {
        RaceEvent raceEvent = raceEventRepository.findById(raceEventId).orElseThrow(() -> new ResourceNotFoundException("Race event not found for ID: " + raceEventId));

        Category newCategory = categoryMapper.toEntity(request);
        newCategory.setRaceEvent(raceEvent);

        Category savedCategory = categoryRepository.save(newCategory);
        return categoryMapper.toResponse(savedCategory);
    }

    public List<CategoryResponse> findAllByRaceId(Long raceEventId) {
        if (!raceEventRepository.existsById(raceEventId)) {
            throw new ResourceNotFoundException("Race event not found for ID: " + raceEventId);
        }
        List<Category> categories = categoryRepository.findByRaceEventId(raceEventId);
        return categoryMapper.toResponse(categories);
    }

    public CategoryResponse update(Long raceEventId, Long categoryId, CategoryRequest request) {
        raceEventRepository.findById(raceEventId).orElseThrow(() -> new ResourceNotFoundException("Race event not found for ID: " + raceEventId));

        Category categoryToUpdate = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found for ID: " + categoryId));

        if (!categoryToUpdate.getRaceEvent().getId().equals(raceEventId)) {
            throw new ResourceNotFoundException("The specified category is not associated with this race event.");
        }

        categoryMapper.updateEntityFromRequest(request, categoryToUpdate);
        Category updatedCategory = categoryRepository.save(categoryToUpdate);

        return categoryMapper.toResponse(updatedCategory);
    }

    public void delete(Long raceEventId, Long categoryId) {
        if (!raceEventRepository.existsById(raceEventId)) {
            throw new ResourceNotFoundException("Race event not found for ID: " + raceEventId);
        }

        Category categoryToDelete = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found for ID: " + categoryId));

        if (!categoryToDelete.getRaceEvent().getId().equals(raceEventId)) {
            throw new ResourceNotFoundException("The specified category is not associated with this race event.");
        }

        categoryRepository.delete(categoryToDelete);
    }
}
