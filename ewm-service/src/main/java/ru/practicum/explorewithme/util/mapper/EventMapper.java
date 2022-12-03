package ru.practicum.explorewithme.util.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventInDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.model.event.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class EventMapper {
    public Event toEvent(EventInDto dto) {
        return Event.builder()
                .title(dto.getTitle())
                .annotation(dto.getAnnotation())
                .description(dto.getDescription())
                .eventDate(LocalDateTime.parse(dto.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .locationLat(dto.getLocation().getLat())
                .locationLon(dto.getLocation().getLon())
                .paid(dto.getPaid())
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration())
                .build();
    }

    public EventShortDto toShortDto(Event model) {
        return EventShortDto.builder()
                .annotation(model.getAnnotation())
                .category(EventShortDto.Category.builder()
                        .id(model.getCategories().getId())
                        .name(model.getCategories().getName())
                        .build())
                .confirmedRequests(model.getConfirmedRequests())
                .description(model.getDescription())
                .eventDate(model.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .id(model.getId())
                .initiator(EventShortDto.User.builder()
                        .id(model.getInitiator().getId())
                        .name(model.getInitiator().getName())
                        .build())
                .paid(model.getPaid())
                .title(model.getTitle())
                .views(model.getViews())
                .build();
    }

    public EventFullDto toFullDto(Event model) {
        return EventFullDto.builder()
                .annotation(model.getAnnotation())
                .category(EventFullDto.Category.builder()
                        .id(model.getCategories().getId())
                        .name(model.getCategories().getName())
                        .build())
                .confirmedRequests(model.getConfirmedRequests())
                .createdOn(model.getCreatedOn())
                .description(model.getDescription())
                .eventDate(model.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .id(model.getId())
                .initiator(EventFullDto.User.builder()
                        .id(model.getInitiator().getId())
                        .name(model.getInitiator().getName())
                        .build())
                .location(EventFullDto.Location.builder()
                        .lat(model.getLocationLat())
                        .lon(model.getLocationLon())
                        .build())
                .paid(model.getPaid())
                .participantLimit(model.getParticipantLimit())
                .publishedOn(model.getPublishedOn())
                .requestModeration(model.getRequestModeration())
                .state(model.getState().name())
                .title(model.getTitle())
                .views(model.getViews())
                .comment(model.getComments() != null ? model.getComments().stream()
                        .map(comment -> EventFullDto.Comment.builder()
                                .id(comment.getId())
                                .text(comment.getText())
                                .userName(comment.getUser().getName())
                                .userEmail(comment.getUser().getEmail())
                                .createdOn(comment.getCreatedOn())
                                .build()).collect(Collectors.toList()) : new ArrayList<>())
                .build();
    }
}
