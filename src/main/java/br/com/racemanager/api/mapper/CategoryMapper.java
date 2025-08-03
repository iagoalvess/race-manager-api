package br.com.racemanager.api.mapper;

import br.com.racemanager.api.dto.CategoryRequest;
import br.com.racemanager.api.dto.CategoryResponse;
import br.com.racemanager.api.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(source = "raceEvent.id", target = "raceEventId")
    CategoryResponse toResponse(Category category);

    Category toEntity(CategoryRequest request);

    List<CategoryResponse> toResponse(List<Category> categories);

    void updateEntityFromRequest(CategoryRequest request, @MappingTarget Category category);
}
