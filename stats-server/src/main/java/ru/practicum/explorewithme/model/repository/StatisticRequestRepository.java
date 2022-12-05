package ru.practicum.explorewithme.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.dto.StatisticDto;
import ru.practicum.explorewithme.model.StatisticRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticRequestRepository extends JpaRepository<StatisticRequest, Long> {
    @Query("SELECT new ru.practicum.explorewithme.dto.StatisticDto(s.app, s.uri, COUNT(s.ip)) " +
            "FROM StatisticRequest AS s " +
            "WHERE s.timestamp BETWEEN :start AND :end AND (:uris  IS NULL OR s.uri IN :uris) " +
            "GROUP BY s.uri, s.app")
    List<StatisticDto> getStatistic(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                    @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.explorewithme.dto.StatisticDto(s.app, s.uri, COUNT (DISTINCT s.ip)) " +
            "FROM StatisticRequest AS s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "AND (:uris  IS NULL OR s.uri IN :uris) " +
            "GROUP BY s.uri, s.app")
    List<StatisticDto> getUniqueStatistic(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                          @Param("uris") List<String> uris);
}