package ru.practicum.explorewithme.controller.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.request.RequestDto;
import ru.practicum.explorewithme.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRequestController {
    RequestService service;

    @GetMapping
    List<RequestDto> findAll(@PathVariable Long userId) {
        return service.findAll(userId);
    }

    @PostMapping
    RequestDto save(@PathVariable Long userId, @RequestParam(name = "eventId") Long eventId) {
        return service.save(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    RequestDto canselRequest(@PathVariable Long userId, @PathVariable(name = "requestId") Long requestId) {
        return service.canselRequest(userId, requestId);
    }
}
