package ru.practicum.explorewithme.util.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.StatisticRequestInDto;
import ru.practicum.explorewithme.dto.StatisticRequestListInDto;
import ru.practicum.explorewithme.model.StatisticRequest;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<StatisticRequest> toModelList(StatisticRequestListInDto dto) {
        return dto.getUris().stream()
                .map(uri -> StatisticRequest.builder()
                        .app(dto.getApp())
                        .uri(uri)
                        .ip(dto.getIp())
                        .timestamp(dto.getTimestamp())
                        .build())
                .collect(Collectors.toList());
    }
}
