package ru.practicum.explorewithme.dto.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

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
}
