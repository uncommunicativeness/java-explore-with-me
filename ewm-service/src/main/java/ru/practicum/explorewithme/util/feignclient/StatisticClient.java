package ru.practicum.explorewithme.util.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "statistic",
        url = "${statistic-server.url}")
public interface StatisticClient {
    @PostMapping("hit")
    void hit(StatisticRequestDto dto);

    @PostMapping("hits")
    void hits(StatisticRequestListDto dto);
}
