package com.meetmap.repository;

import com.meetmap.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByCityIgnoreCase(String city);

    List<Event> findByCategoryIgnoreCase(String category);

    List<Event> findByDateTimeAfter(LocalDateTime dateTime);

    List<Event> findByTitleContainingIgnoreCase(String keyword);
}
