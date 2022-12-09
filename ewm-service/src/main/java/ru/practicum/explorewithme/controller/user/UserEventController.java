package ru.practicum.explorewithme.controller.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.CommentInDto;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventInDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.EventUpdateDto;
import ru.practicum.explorewithme.dto.request.RequestDto;
import ru.practicum.explorewithme.service.CommentService;
import ru.practicum.explorewithme.service.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserEventController {
    EventService service;
    CommentService commentService;

    @PostMapping
    EventFullDto save(@PathVariable Long userId, @Validated @RequestBody EventInDto dto) {
        return service.save(userId, dto);
    }

    @GetMapping
    List<EventShortDto> findAll(@PathVariable Long userId,
                                @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        return service.findAll(userId, from, size);
    }

    @PatchMapping
    EventFullDto update(@PathVariable Long userId, @RequestBody EventUpdateDto dto) {
        return service.update(userId, dto);
    }

    @GetMapping("/{eventId}")
    EventFullDto getById(@PathVariable Long userId, @PathVariable Long eventId) {
        return service.getById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    EventFullDto canselEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return service.canselEvent(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    List<RequestDto> getRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        return service.getRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    RequestDto confirm(@PathVariable Long userId, @PathVariable Long eventId, @PathVariable Long reqId) {
        return service.confirm(userId, eventId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    RequestDto reject(@PathVariable Long userId, @PathVariable Long eventId, @PathVariable Long reqId) {
        return service.reject(userId, eventId, reqId);
    }

    @PostMapping("/{eventId}/comment")
    CommentDto saveComment(@Validated @RequestBody CommentInDto dto, @PathVariable Long userId, @PathVariable Long eventId) {
        return commentService.saveComment(dto, userId, eventId);
    }
}
