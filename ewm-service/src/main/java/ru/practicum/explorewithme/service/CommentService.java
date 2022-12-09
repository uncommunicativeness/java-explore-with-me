package ru.practicum.explorewithme.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.CommentInDto;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.model.Comment;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.repository.CommentRepository;
import ru.practicum.explorewithme.model.repository.EventRepository;
import ru.practicum.explorewithme.model.repository.UserRepository;
import ru.practicum.explorewithme.util.mapper.CommentMapper;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentService {
    EventRepository eventRepository;
    UserRepository userRepository;
    CommentRepository commentRepository;
    CommentMapper mapper;

    public List<CommentDto> findAllByEvent(Long eventId) {
        return commentRepository.findByEvent_Id(eventId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto saveComment(CommentInDto dto, Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find user with id=%d", userId)));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find event with id=%d", eventId)));

        Comment comment = mapper.toModel(dto);
        comment.setUser(user);
        comment.setEvent(event);
        comment = commentRepository.save(comment);

        return mapper.toDto(comment);
    }

    @Transactional
    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public List<CommentDto> findAll() {
        return commentRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public CommentDto findById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(String.format("Failed to find comment with id=%d", commentId)));

        return mapper.toDto(comment);
    }
}
