package ru.practicum.explorewithme.controller.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.CategoryInDto;
import ru.practicum.explorewithme.service.CategoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCategoryController {
    CategoryService service;

    @PostMapping
    public CategoryDto save(@Validated @RequestBody CategoryInDto dto) {
        return service.save(dto);
    }

    @PatchMapping
    public CategoryDto update(@Validated @RequestBody CategoryDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{catId}")
    public void delete(@PathVariable Long catId) {
        service.delete(catId);
    }
}
