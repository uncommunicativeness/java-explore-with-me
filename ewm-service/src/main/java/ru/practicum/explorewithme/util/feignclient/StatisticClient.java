package ru.practicum.explorewithme.util.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "statistic",
        path = "hit",
        url = "${statistic-server.url}")
public interface StatisticClient {
    @PostMapping
    void hit(StatisticRequestDto dto);
}
