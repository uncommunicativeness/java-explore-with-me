package ru.practicum.explorewithme.dto.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventUpdateDto {
    String annotation;
    Long category;
    String description;
    String eventDate;
    Long eventId;
    Boolean paid;
    Integer participantLimit;
    String title;
}