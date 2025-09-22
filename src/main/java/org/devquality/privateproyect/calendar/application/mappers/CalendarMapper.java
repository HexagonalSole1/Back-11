package org.devquality.privateproyect.calendar.application.mappers;

import org.devquality.privateproyect.calendar.domain.entities.Calendar;
import org.devquality.privateproyect.calendar.infrastructure.dtos.request.CalendarRequest;
import org.devquality.privateproyect.calendar.infrastructure.dtos.response.CalendarResponse;
import org.springframework.stereotype.Component;

@Component
public class CalendarMapper {

    public Calendar toEntity(CalendarRequest request) {
        Calendar calendar = new Calendar();
        calendar.setTitle(request.getTitle());
        calendar.setDescription(request.getDescription());
        calendar.setStartDate(request.getStartDate());
        calendar.setEndDate(request.getEndDate());
        calendar.setEventType(request.getEventType());
        calendar.setIsAllDay(request.getIsAllDay());
        calendar.setConfirmedJulian(request.getConfirmedJulian());
        calendar.setConfirmedDayani(request.getConfirmedDayani());
        return calendar;
    }

    public CalendarResponse toResponse(Calendar calendar) {
        return CalendarResponse.builder()
                .id(calendar.getId())
                .title(calendar.getTitle())
                .description(calendar.getDescription())
                .startDate(calendar.getStartDate())
                .endDate(calendar.getEndDate())
                .eventType(calendar.getEventType())
                .isAllDay(calendar.getIsAllDay())
                .confirmedJulian(calendar.getConfirmedJulian())
                .confirmedDayani(calendar.getConfirmedDayani())
                .createdAt(calendar.getCreatedAt())
                .updatedAt(calendar.getUpdatedAt())
                .build();
    }

    public void updateEntity(Calendar calendar, CalendarRequest request) {
        calendar.setTitle(request.getTitle());
        calendar.setDescription(request.getDescription());
        calendar.setStartDate(request.getStartDate());
        calendar.setEndDate(request.getEndDate());
        calendar.setEventType(request.getEventType());
        calendar.setIsAllDay(request.getIsAllDay());
        calendar.setConfirmedJulian(request.getConfirmedJulian());
        calendar.setConfirmedDayani(request.getConfirmedDayani());
    }
}
