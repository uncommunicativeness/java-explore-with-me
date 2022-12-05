package ru.practicum.explorewithme.model.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.explorewithme.model.User;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    List<User> findByIdIn(Collection<Long> ids, Pageable pageable);

}