package ru.practicum.explorewithme.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.explorewithme.model.request.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("select r from Request r where r.event.initiator.id = :initiator")
    List<Request> findAllByInitiator(@PathVariable("initiator") Long initiator);
}