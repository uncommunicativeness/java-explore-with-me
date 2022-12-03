package ru.practicum.explorewithme.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    Long id;
    String text;
    @JsonProperty("user_name")
    String userName;
    @JsonProperty("user_email")
    String userEmail;

    LocalDateTime createdOn;
}
