package ru.practicum.explorewithme.controller.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCommentController {
    CommentService service;

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long commentId) {
        service.delete(commentId);
    }
}
