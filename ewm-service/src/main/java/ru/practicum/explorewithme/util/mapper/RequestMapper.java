package ru.practicum.explorewithme.util.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.request.RequestDto;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.request.Request;

@Component
public class RequestMapper {

    public Request toRequest(User requester, Event event) {
        return Request.builder()
                .requester(requester)
                .event(event)
                .build();
    }

    public RequestDto toDto(Request model) {
        return RequestDto.builder()
                .id(model.getId())
                .created(model.getCreated())
                .event(model.getEvent().getId())
                .requester(model.getRequester().getId())
                .status(model.getState().name())
                .build();
    }
}
