package ru.practicum.explorewithme.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.CompilationInDto;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.model.Compilation;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.repository.CompilationRepository;
import ru.practicum.explorewithme.model.repository.EventRepository;
import ru.practicum.explorewithme.util.mapper.CompilationMapper;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompilationService {
    CompilationRepository compilationRepository;
    EventRepository eventRepository;
    CompilationMapper mapper;

    @Transactional
    public CompilationDto save(CompilationInDto dto) {
        Compilation compilation = compilationRepository.save(mapper.toCompilation(dto));

        eventRepository.findAllById(dto.getEvents()).forEach(event -> {
            event.addCompilation(compilation);
            compilation.addEvent(event);
        });

        return mapper.toDto(compilation);
    }

    @Transactional
    public void delete(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find compilation with id=%d", compId)));

        compilation.getEvents().forEach(event -> event.removeCompilation(compilation));
        compilation.getEvents().forEach(compilation::removeEvent);
        compilationRepository.delete(compilation);
    }

    @Transactional
    public void deleteEvent(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find compilation with id=%d", compId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find event with id=%d", eventId)));

        compilation.removeEvent(event);
        event.removeCompilation(compilation);
    }

    @Transactional
    public void addEvent(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find compilation with id=%d", compId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find event with id=%d", eventId)));

        compilation.addEvent(event);
        event.addCompilation(compilation);
    }

    @Transactional
    public void unpin(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find compilation with id=%d", compId)));
        compilation.setPinned(false);
    }

    @Transactional
    public void pin(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find compilation with id=%d", compId)));
        compilation.setPinned(true);
    }

    public List<CompilationDto> findAll(Boolean pinned, Integer from, Integer size) {
        return compilationRepository.findByPinned(pinned, PageRequest.of(from / size, size)).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public CompilationDto findById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find compilation with id=%d", compId)));

        return mapper.toDto(compilation);
    }
}
