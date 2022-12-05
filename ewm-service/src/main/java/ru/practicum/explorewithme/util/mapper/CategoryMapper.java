package ru.practicum.explorewithme.util.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.CategoryInDto;
import ru.practicum.explorewithme.model.Category;

@Component
public class CategoryMapper {
    public Category toCategory(CategoryInDto dto) {
        return Category.builder()
                .name(dto.getName())
                .build();
    }

    public CategoryDto toDto(Category model) {
        return CategoryDto.builder()
                .id(model.getId())
                .name(model.getName())
                .build();
    }
}
