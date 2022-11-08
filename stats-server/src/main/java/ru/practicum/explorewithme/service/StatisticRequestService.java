package ru.practicum.explorewithme.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.StatisticDto;
import ru.practicum.explorewithme.dto.StatisticRequestInDto;
import ru.practicum.explorewithme.model.repository.StatisticRequestRepository;
import ru.practicum.explorewithme.util.mapper.StatisticRequestMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticRequestService {
    StatisticRequestRepository repository;
    StatisticRequestMapper mapper;

    @Transactional
    public void hit(StatisticRequestInDto dto) {
        repository.save(mapper.toModel(dto));
    }

    public List<StatisticDto> getStatistic(String startString, String endString, List<String> uris, Boolean unique) {
        LocalDateTime start = LocalDateTime.parse(startString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime end = LocalDateTime.parse(endString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (uris.contains("/events")) {
            uris = null;
        }

        if (unique) {
            return repository.getUniqueStatistic(start, end, uris);
        } else {
            return repository.getStatistic(start, end, uris);
        }
    }
}
