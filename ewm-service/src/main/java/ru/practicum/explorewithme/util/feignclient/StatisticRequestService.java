package ru.practicum.explorewithme.util.feignclient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.event.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatisticRequestService {

    final StatisticClient client;
    @Value("${spring.application.name}")
    String appName;

    public void hit(HttpServletRequest request) {
        client.hit(StatisticRequestDto.builder()
                .app(appName)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
    }

    public void hit(HttpServletRequest request, EventShortDto dto) {
        client.hit(StatisticRequestDto.builder()
                .app(appName)
                .uri(String.join("/", request.getRequestURI(), dto.getId().toString()))
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
    }
}
