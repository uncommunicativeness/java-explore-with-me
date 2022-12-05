package ru.practicum.explorewithme.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.user.UserFullDto;
import ru.practicum.explorewithme.dto.user.UserInDto;
import ru.practicum.explorewithme.exception.CreateException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.model.repository.UserRepository;
import ru.practicum.explorewithme.util.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository repository;
    UserMapper mapper;

    @Transactional
    public UserFullDto save(UserInDto dto) {
        User user;
        try {
            user = repository.save(mapper.toUser(dto));
        } catch (Exception e) {
            log.warn("Failed to create user, error: {}", e.getMessage());
            throw new CreateException(e);
        }

        return mapper.toDto(user);
    }

    public List<UserFullDto> findAll(List<Long> ids, Integer from, Integer size) {
        return repository.findByIdIn(ids, PageRequest.of(from / size, size)).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find user with id=%d", userId)));

        repository.delete(user);
    }
}
