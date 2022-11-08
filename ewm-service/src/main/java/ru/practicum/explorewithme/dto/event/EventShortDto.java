package ru.practicum.explorewithme.dto.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventShortDto {
    Long id;
    String annotation;
    String description;
    Category category;
    Integer confirmedRequests;
    String eventDate;
    User initiator;
    Boolean paid;
    String title;
    Integer views;

    @Data
    @Builder
    public static class User {
        Long id;
        String name;
    }

    @Data
    @Builder
    public static class Category {
        Long id;
        String name;
    }
}
