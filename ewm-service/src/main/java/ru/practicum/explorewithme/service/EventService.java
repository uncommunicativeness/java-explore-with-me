package ru.practicum.explorewithme.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.event.*;
import ru.practicum.explorewithme.dto.request.RequestDto;
import ru.practicum.explorewithme.exception.ConflictException;
import ru.practicum.explorewithme.exception.CreateException;
import ru.practicum.explorewithme.exception.ForbiddenException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.event.State;
import ru.practicum.explorewithme.model.repository.CategoryRepository;
import ru.practicum.explorewithme.model.repository.EventRepository;
import ru.practicum.explorewithme.model.repository.RequestRepository;
import ru.practicum.explorewithme.model.repository.UserRepository;
import ru.practicum.explorewithme.model.request.Request;
import ru.practicum.explorewithme.util.feignclient.StatisticRequestService;
import ru.practicum.explorewithme.util.mapper.EventMapper;
import ru.practicum.explorewithme.util.mapper.RequestMapper;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventService {
    EventRepository eventRepository;
    CategoryRepository categoryRepository;
    UserRepository userRepository;
    RequestRepository requestRepository;
    StatisticRequestService service;
    EventMapper eventMapper;
    RequestMapper requestMapper;

    @Transactional
    public EventFullDto save(Long userId, EventInDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find user with id=%d", userId)));

        Category category = categoryRepository.findById(dto.getCategory())
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find category with id=%d", dto.getCategory())));

        Event event = eventMapper.toEvent(dto);
        event.setInitiator(user);
        event.setCategories(category);
        event.setState(State.PENDING);

        try {
            event = eventRepository.save(event);
        } catch (Exception e) {
            log.warn("Failed to create event, error: {}", e.getMessage());
            throw new CreateException(e);
        }

        return eventMapper.toFullDto(event);
    }

    public List<EventShortDto> findAll(Long userId, Integer from, Integer size) {
        return eventRepository.findAllByInitiatorId(userId, PageRequest.of(from / size, size)).stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventFullDto update(Long userId, EventUpdateDto dto) {
        Event event = checkAndGetEvent(userId, dto.getEventId());

        LocalDateTime rangeStart = LocalDateTime.parse(dto.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
        LocalDateTime now = LocalDateTime.now();
        if (rangeStart.isBefore(now.plusHours(2))) {
            throw new ConflictException("Incorrect date for edited event");
        }

        // Изменить можно только отмененные события или события в состоянии ожидания модерации
        if (event.getState() == State.PUBLISHED) {
            throw new ConflictException("Unable to edit published event");
        }

        // Если редактируется отменённое событие, то оно автоматически переходит в состояние ожидания модерации
        if (event.getState() == State.CANCELED) {
            event.setState(State.PENDING);
        }

        if (dto.getAnnotation() != null) {
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getCategory() != null) {
            Category category = categoryRepository.findById(dto.getCategory())
                    .orElseThrow(() -> new NotFoundException(String.format("Failed to find category with id=%d", dto.getCategory())));
            event.setCategories(category);
        }
        if (dto.getDescription() != null) {
            event.setDescription(dto.getDescription());
        }
        if (dto.getEventDate() != null) {
            event.setEventDate(rangeStart);
        }
        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }
        if (dto.getParticipantLimit() != null) {
            event.setParticipantLimit(dto.getParticipantLimit());
        }
        if (dto.getTitle() != null) {
            event.setTitle(dto.getTitle());
        }

        return eventMapper.toFullDto(event);
    }

    @Transactional
    public EventFullDto update(Long eventId, EventAdminUpdateDto dto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find event with id=%d", eventId)));

        event.setAnnotation(dto.getAnnotation());

        Category category = categoryRepository.findById(dto.getCategory())
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find category with id=%d", dto.getCategory())));
        event.setCategories(category);

        event.setDescription(dto.getDescription());
        event.setEventDate(LocalDateTime.parse(dto.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        if (dto.getLocation() != null && dto.getLocation().getLat() != null && dto.getLocation().getLon() != null) {
            event.setLocationLat(dto.getLocation().getLat());
            event.setLocationLon(dto.getLocation().getLon());
        }
        event.setPaid(dto.getPaid());
        event.setParticipantLimit(dto.getParticipantLimit());
        event.setRequestModeration(dto.getRequestModeration());
        event.setRequestModeration(dto.getRequestModeration());
        event.setTitle(dto.getTitle());

        return eventMapper.toFullDto(event);
    }

    public EventFullDto getById(Long userId, Long eventId) {
        Event event = checkAndGetEvent(userId, eventId);

        return eventMapper.toFullDto(event);
    }

    @Transactional
    public EventFullDto canselEvent(Long userId, Long eventId) {
        Event event = checkAndGetEvent(userId, eventId);

        if (event.getState() != State.PENDING) {
            throw new ConflictException("You can only cancel an event in the moderation pending state");
        }
        event.setState(State.CANCELED);

        return eventMapper.toFullDto(event);
    }

    public List<RequestDto> getRequests(Long userId, Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException(String.format("Failed to find event with id=%d", eventId));
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("Failed to find user with id=%d", userId));
        }

        List<Request> requests = requestRepository.findAllByInitiator(userId);

        return requests.stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public RequestDto confirm(Long userId, Long eventId, Long reqId) {
        Event event = checkAndGetEvent(userId, eventId);

        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find request with id=%d", reqId)));

        if (!Objects.equals(request.getEvent(), event)) {
            throw new ConflictException("Request does not apply to this event");
        }

        // если для события лимит заявок равен 0, то подтверждение заявок не требуется
        // если для события отключена пре-модерация заявок, то подтверждение заявок не требуется
        // нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие
        if (event.getParticipantLimit() == 0) {
            throw new ConflictException("Unable to confirm request: confirmation is not required");
        } else if (!event.getRequestModeration()) {
            throw new ConflictException("Unable to confirm request: confirmation is not required");
        } else if (Objects.equals(event.getConfirmedRequests(), event.getParticipantLimit())) {
            throw new ConflictException("Unable to confirm request: limit on applications for event has been reached");
        }

        request.setState(ru.practicum.explorewithme.model.request.State.CONFIRMED);
        // Добавляем еще одно подтверждение
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);

        // если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить
        if (Objects.equals(event.getConfirmedRequests(), event.getParticipantLimit())) {
            event.getRequests().stream()
                    .filter(eventRequest -> eventRequest.getState() == ru.practicum.explorewithme.model.request.State.PENDING)
                    .forEach(eventRequest -> eventRequest.setState(ru.practicum.explorewithme.model.request.State.CANCELED));
        }

        return requestMapper.toDto(request);
    }

    @Transactional
    public RequestDto reject(Long userId, Long eventId, Long reqId) {
        Event event = checkAndGetEvent(userId, eventId);

        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find request with id=%d", reqId)));

        if (!Objects.equals(request.getEvent(), event)) {
            throw new ConflictException("Request does not apply to this event");
        }

        request.setState(ru.practicum.explorewithme.model.request.State.REJECTED);

        return requestMapper.toDto(request);
    }

    private Event checkAndGetEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find event with id=%d", eventId)));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ForbiddenException(String.format("The user with id=%d does not have rights to edit the event with id=%d",
                    userId, event.getId()));
        }

        return event;
    }

    @Transactional
    public EventFullDto findById(HttpServletRequest request, Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find event with id=%d", id)));

        // событие должно быть опубликовано
        if (event.getState() != State.PUBLISHED) {
            throw new ConflictException("The event must be published");
        }

        event.setViews(event.getViews() + 1);

        // Сохраняем информацию в сервисе статистики
        service.hit(request);

        return eventMapper.toFullDto(event);
    }

    public List<EventShortDto> findAll(HttpServletRequest request,
                                       String text,
                                       List<Long> categories,
                                       Boolean paid,
                                       String rangeStart,
                                       String rangeEnd,
                                       Boolean onlyAvailable,
                                       String sortString,
                                       Integer from,
                                       Integer size) {
        Sort sort;
        switch (sortString) {
            case "EVENT_DATE":
                sort = Sort.by("eventDate");
                break;
            case "VIEWS":
                sort = Sort.by("views");
                break;
            case "ID":
                sort = Sort.by("id");
                break;
            default:
                throw new ConflictException("Incorrect sort params");
        }

        Page<Event> events = eventRepository.findAll((root, query, builder) ->
                        builder.and(builder.equal(
                                        root.get("state"), State.PUBLISHED),
                                (categories != null) ?
                                        root.get("categories").in(categories) : root.isNotNull(),
                                (paid != null) ?
                                        builder.equal(root.get("paid"), paid) : root.isNotNull(),
                                (rangeStart != null && rangeEnd != null) ?
                                        builder.and(
                                                builder.greaterThan(root.get("eventDate"), LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))),
                                                builder.lessThan(root.get("eventDate"), LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                        ) : builder.greaterThan(root.get("eventDate"), LocalDateTime.now()),
                                (onlyAvailable) ? builder.or(
                                        builder.equal(root.get("participantLimit"), 0),
                                        builder.and(
                                                builder.notEqual(root.get("participantLimit"), 0),
                                                builder.greaterThan(root.get("participantLimit"), root.get("confirmedRequests"))
                                        )) : root.isNotNull(),
                                (text != null) ?
                                        builder.or(
                                                builder.like(builder.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"),
                                                builder.like(builder.lower(root.get("description")), "%" + text.toLowerCase() + "%")
                                        ) : root.isNotNull()
                        ),
                PageRequest.of(from / size, size, sort));

        // Сохраняем информацию в сервисе статистики
        service.hitAll(request, events.stream()
                .map(Event::getId)
                .map(id -> Long.toString(id))
                .collect(Collectors.toList())
        );

        return events.stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    public List<EventFullDto> findAll(List<Long> users,
                                      List<String> states,
                                      List<Long> categories,
                                      String rangeStart,
                                      String rangeEnd,
                                      Integer from,
                                      Integer size) {
        Page<Event> events = eventRepository.findAll((root, query, builder) ->
                        builder.and(
                                (users != null) ? root.get("initiator").in(users) : root.isNotNull(),
                                (states != null) ? root.get("state").in(states.stream().map(State::valueOf).collect(Collectors.toList())) : root.isNotNull(),
                                (categories != null) ? root.get("categories").in(categories) : root.isNotNull(),
                                (!rangeStart.isEmpty() && !rangeEnd.isEmpty()) ?
                                        builder.and(
                                                builder.greaterThan(root.get("eventDate"), LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))),
                                                builder.lessThan(root.get("eventDate"), LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                        ) : builder.greaterThan(root.get("eventDate"), LocalDateTime.now())
                        ),
                PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id")));
        return events.stream()
                .map(eventMapper::toFullDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventFullDto publish(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find event with id=%d", eventId)));

        // дата начала события должна быть не ранее чем за час от даты публикации
        if (event.getEventDate().isBefore(LocalDateTime.now().minusHours(2))) {
            throw new ConflictException("The start date of the event must be no earlier than one hour from the date of publication");
        }
        // событие должно быть в состоянии ожидания публикации
        if (event.getState() != State.PENDING) {
            throw new ConflictException("The event must be in a publish pending state");
        }
        event.setState(State.PUBLISHED);

        return eventMapper.toFullDto(event);
    }

    @Transactional
    public EventFullDto reject(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find event with id=%d", eventId)));

        // событие не должно быть опубликовано
        if (event.getState() == State.PUBLISHED) {
            throw new ConflictException("The event should not be published.");
        }
        event.setState(State.CANCELED);

        return eventMapper.toFullDto(event);
    }
}
