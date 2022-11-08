package ru.practicum.explorewithme.controller.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.user.UserFullDto;
import ru.practicum.explorewithme.dto.user.UserInDto;
import ru.practicum.explorewithme.service.UserService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminUserController {
    UserService service;

    @PostMapping
    public UserFullDto save(@Validated @RequestBody UserInDto dto) {
        return service.save(dto);
    }

    @GetMapping
    List<UserFullDto> findAll(@RequestParam List<Long> ids,
                              @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                              @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        return service.findAll(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        service.delete(userId);
    }
}
