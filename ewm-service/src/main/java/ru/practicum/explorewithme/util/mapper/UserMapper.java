package ru.practicum.explorewithme.util.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.user.UserFullDto;
import ru.practicum.explorewithme.dto.user.UserInDto;
import ru.practicum.explorewithme.dto.user.UserShortDto;
import ru.practicum.explorewithme.model.User;

@Component
public class UserMapper {
    public User toUser(UserInDto dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public UserFullDto toDto(User model) {
        return UserFullDto.builder()
                .id(model.getId())
                .name(model.getName())
                .email(model.getEmail())
                .build();
    }

    public UserShortDto toShortDto(User model) {
        return UserShortDto.builder()
                .id(model.getId())
                .name(model.getName())
                .build();
    }
}
