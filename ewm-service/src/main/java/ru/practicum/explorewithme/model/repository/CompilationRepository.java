package ru.practicum.explorewithme.model.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.model.Compilation;

import java.util.List;

public interface CompilationRepository extends PagingAndSortingRepository<Compilation, Long> {
    @Query("select c from Compilation c where c.pinned = coalesce(:pinned, c.pinned)")
    List<Compilation> findByPinned(@Param("pinned") Boolean pinned, Pageable pageable);
}