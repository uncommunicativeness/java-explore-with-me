package ru.practicum.explorewithme.controller.common;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommonCompilationController {
    CompilationService service;

    @GetMapping
    List<CompilationDto> findAll(@RequestParam(required = false) Boolean pinned,
                                 @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                 @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        return service.findAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    CompilationDto findById(@PathVariable Long compId) {
        return service.findById(compId);
    }
}
