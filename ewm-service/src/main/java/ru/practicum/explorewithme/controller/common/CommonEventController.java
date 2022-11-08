package ru.practicum.explorewithme.controller.common;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommonEventController {
    EventService service;

    @GetMapping("/{id}")
    EventFullDto findById(HttpServletRequest request, @PathVariable Long id) {
        return service.findById(request, id);
    }

    @GetMapping
    List<EventShortDto> findAll(HttpServletRequest request,
                                @RequestParam(required = false) String text,
                                @RequestParam(required = false) List<Long> categories,
                                @RequestParam(required = false) Boolean paid,
                                @RequestParam(required = false) String rangeStart,
                                @RequestParam(required = false) String rangeEnd,
                                @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                @RequestParam(required = false, defaultValue = "ID") String sort,
                                @RequestParam(required = false, defaultValue = "0") Integer from,
                                @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return service.findAll(request, text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }
}
