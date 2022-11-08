package ru.practicum.explorewithme.controller.common;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommonCategoryController {
    CategoryService service;

    @GetMapping
    List<CategoryDto> findAll(@PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                              @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        return service.findAll(from, size);
    }

    @GetMapping("/{catId}")
    CategoryDto findById(@PathVariable Long catId) {
        return service.findById(catId);
    }
}
