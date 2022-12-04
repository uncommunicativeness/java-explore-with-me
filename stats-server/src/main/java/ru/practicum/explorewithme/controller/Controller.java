package ru.practicum.explorewithme.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.StatisticDto;
import ru.practicum.explorewithme.dto.StatisticRequestInDto;
import ru.practicum.explorewithme.dto.StatisticRequestListInDto;
import ru.practicum.explorewithme.service.StatisticRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Controller {
    StatisticRequestService service;

    @PostMapping("/hit")
    public void hit(@RequestBody StatisticRequestInDto dto) {
        service.hit(dto);
    }

    @PostMapping("/hits")
    public void hits(@RequestBody StatisticRequestListInDto dto) {
        service.hits(dto);
    }

    @GetMapping("/stats")
    List<StatisticDto> getStatistic(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique
    ) {
        return service.getStatistic(start, end, uris, unique);
    }
}
