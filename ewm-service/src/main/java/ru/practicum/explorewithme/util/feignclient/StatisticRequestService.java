package ru.practicum.explorewithme.util.feignclient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public void hitAll(HttpServletRequest request, List<String> ids) {
        client.hits(StatisticRequestListDto.builder()
                .app(appName)
                .uris(ids.stream()
                        .map(id -> String.join("/", request.getRequestURI(), id)).collect(Collectors.toList()))
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
    }
}