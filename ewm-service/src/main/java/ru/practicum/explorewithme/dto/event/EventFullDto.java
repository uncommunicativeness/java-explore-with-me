package ru.practicum.explorewithme.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {
    String annotation;
    Category category;
    Integer confirmedRequests;
    LocalDateTime createdOn;
    String description;
    String eventDate;
    Long id;
    User initiator;
    Location location;
    Boolean paid;
    Integer participantLimit;
    LocalDateTime publishedOn;
    Boolean requestModeration;
    String state;
    String title;
    Integer views;
    List<Comment> comment;

    @Data
    @Builder
    public static class Category {
        Long id;
        String name;
    }

    @Data
    @Builder
    public static class User {
        Long id;
        String name;
    }

    @Data
    @Builder
    public static class Location {
        Float lat;
        Float lon;
    }

    @Data
    @Builder
    public static class Comment {
        Long id;
        String text;
        @JsonProperty("user_name")
        String userName;
        @JsonProperty("user_email")
        String userEmail;
        LocalDateTime createdOn;
    }
}
