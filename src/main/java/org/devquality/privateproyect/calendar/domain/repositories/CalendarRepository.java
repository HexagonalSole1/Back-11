package org.devquality.privateproyect.calendar.domain.repositories;

import org.devquality.privateproyect.calendar.domain.entities.Calendar;
import org.devquality.privateproyect.calendar.domain.enums.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findByEventType(EventType eventType);
}
