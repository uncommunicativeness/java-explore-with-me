package ru.practicum.explorewithme.controller.common;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommonCommentController {
    CommentService service;

    @GetMapping
    List<CommentDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{commentId}")
    CommentDto findById(@PathVariable Long commentId) {
        return service.findById(commentId);
    }

    @GetMapping("/events/{eventId}")
    List<CommentDto> findAllByEventId(@PathVariable Long eventId) {
        return service.findAllByEvent(eventId);
    }
}
