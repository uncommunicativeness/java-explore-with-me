package ru.practicum.explorewithme.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.request.RequestDto;
import ru.practicum.explorewithme.exception.ConflictException;
import ru.practicum.explorewithme.exception.ForbiddenException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.repository.EventRepository;
import ru.practicum.explorewithme.model.repository.RequestRepository;
import ru.practicum.explorewithme.model.repository.UserRepository;
import ru.practicum.explorewithme.model.request.Request;
import ru.practicum.explorewithme.model.request.State;
import ru.practicum.explorewithme.util.mapper.RequestMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequestService {
    RequestRepository requestRepository;
    UserRepository userRepository;
    EventRepository eventRepository;
    RequestMapper mapper;

    public List<RequestDto> findAll(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find user with id=%d", userId)));

        return user.getRequests().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public RequestDto save(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find event with id=%d", eventId)));

        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find user with id=%d", userId)));


        if (event.getInitiator().equals(requester)) {
            throw new ConflictException("The initiator of the event cannot add a request to participate in his event");
        }

        if (event.getState() != ru.practicum.explorewithme.model.event.State.PUBLISHED) {
            throw new ConflictException("You can't participate in an unpublished event");
        }

        if (Objects.equals(event.getConfirmedRequests(), event.getParticipantLimit())) {
            throw new ConflictException("The limit of requests for participation has been reached");
        }

        Request request = requestRepository.save(mapper.toRequest(requester, event));

        if (event.getRequestModeration()) {
            request.setState(State.PENDING);
        } else {
            request.setState(State.CONFIRMED);
        }

        return mapper.toDto(request);
    }

    @Transactional
    public RequestDto canselRequest(Long userId, Long requestId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find user with id=%d", userId)));

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find request with id=%d", requestId)));

        if (!Objects.equals(request.getRequester(), requester)) {
            throw new ForbiddenException(String.format("User %d is not allowed to cancel request %d",
                    userId, requestId));
        }
        request.setState(State.CANCELED);

        return mapper.toDto(request);
    }
}
