package ru.practicum.explorewithme.util.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.CompilationInDto;
import ru.practicum.explorewithme.model.Compilation;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class CompilationMapper {
    public Compilation toCompilation(CompilationInDto dto) {
        return Compilation.builder()
                .title(dto.getTitle())
                .pinned(dto.getPinned())
                .build();
    }

    public CompilationDto toDto(Compilation model) {
        return CompilationDto.builder()
                .id(model.getId())
                .title(model.getTitle())
                .pinned(model.getPinned())
                .events(model.getEvents() != null ? model.getEvents().stream()
                        .map(event -> CompilationDto.Event.builder()
                                .id(event.getId())
                                .annotation(event.getAnnotation())
                                .description(event.getDescription())
                                .category(CompilationDto.Event.Category.builder()
                                        .id(event.getCategories().getId())
                                        .name(event.getCategories().getName())
                                        .build())
                                .confirmedRequests(event.getConfirmedRequests())
                                .eventDate(event.getEventDate())
                                .initiator(CompilationDto.Event.User.builder()
                                        .id(event.getInitiator().getId())
                                        .name(event.getInitiator().getName())
                                        .build())
                                .paid(event.getPaid())
                                .title(event.getTitle())
                                .views(event.getViews())
                                .build())
                        .collect(Collectors.toList()) : new ArrayList<>())
                .build();
    }
}
