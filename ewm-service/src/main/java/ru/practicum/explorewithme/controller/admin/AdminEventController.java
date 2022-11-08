package ru.practicum.explorewithme.controller.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.event.EventAdminUpdateDto;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.service.EventService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminEventController {
    EventService service;

    @GetMapping
    List<EventFullDto> findAll(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false, defaultValue = "") String rangeStart,
            @RequestParam(required = false, defaultValue = "") String rangeEnd,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return service.findAll(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    EventFullDto update(@PathVariable Long eventId, @RequestBody EventAdminUpdateDto dto) {
        return service.update(eventId, dto);
    }

    @PatchMapping("/{eventId}/publish")
    EventFullDto publish(@PathVariable Long eventId) {
        return service.publish(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    EventFullDto reject(@PathVariable Long eventId) {
        return service.reject(eventId);
    }
}
