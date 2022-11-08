package ru.practicum.explorewithme.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.CategoryInDto;
import ru.practicum.explorewithme.exception.CreateException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.model.repository.CategoryRepository;
import ru.practicum.explorewithme.util.mapper.CategoryMapper;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryRepository repository;
    CategoryMapper mapper;

    @Transactional
    public CategoryDto save(CategoryInDto dto) {
        Category category;

        try {
            category = repository.save(mapper.toCategory(dto));
        } catch (Exception e) {
            log.warn("Failed to create category, error: {}", e.getMessage());
            throw new CreateException(e);
        }

        return mapper.toDto(category);
    }

    @Transactional
    public CategoryDto update(CategoryDto dto) {
        Category category = repository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find category with id=%d", dto.getId())));

        category.setName(dto.getName());
        return mapper.toDto(category);
    }

    @Transactional
    public void delete(Long catId) {
        Category category = repository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find category with id=%d", catId)));

        repository.delete(category);
    }

    public List<CategoryDto> findAll(Integer from, Integer size) {
        return repository.findAll(PageRequest.of(from / size, size)).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public CategoryDto findById(Long catId) {
        Category category = repository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find category with id=%d", catId)));

        return mapper.toDto(category);
    }
}
