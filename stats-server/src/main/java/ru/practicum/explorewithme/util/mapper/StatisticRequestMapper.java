package ru.practicum.explorewithme.util.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.StatisticRequestInDto;
import ru.practicum.explorewithme.model.StatisticRequest;

@Component
public class StatisticRequestMapper {
    public StatisticRequest toModel(StatisticRequestInDto dto) {
        return StatisticRequest.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .timestamp(dto.getTimestamp())
                .build();
    }

}
