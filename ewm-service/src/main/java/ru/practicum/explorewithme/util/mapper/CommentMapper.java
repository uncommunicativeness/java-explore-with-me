package ru.practicum.explorewithme.util.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.CommentInDto;
import ru.practicum.explorewithme.model.Comment;

@Component
public class CommentMapper {
    public Comment toModel(CommentInDto dto) {
        return Comment.builder()
                .text(dto.getText())
                .build();
    }

    public CommentDto toDto(Comment model) {
        return CommentDto.builder()
                .id(model.getId())
                .text(model.getText())
                .userName(model.getUser().getName())
                .userEmail(model.getUser().getEmail())
                .createdOn(model.getCreatedOn())
                .build();
    }
}
