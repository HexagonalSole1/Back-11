package org.devquality.privateproyect.calendar.application.services;

import org.devquality.privateproyect.calendar.infrastructure.dtos.request.CalendarRequest;
import org.devquality.privateproyect.calendar.infrastructure.dtos.response.CalendarResponse;
import org.devquality.privateproyect.core.infrastructure.dtos.response.BaseResponse;

import java.util.List;

public interface ICalendarService {
    BaseResponse<CalendarResponse> createEvent(CalendarRequest request);
    BaseResponse<CalendarResponse> getEventById(Long id);
    BaseResponse<List<CalendarResponse>> getAllEvents();
    BaseResponse<CalendarResponse> updateEvent(Long id, CalendarRequest request);
    BaseResponse<Object> deleteEvent(Long id);
    BaseResponse<List<CalendarResponse>> getEventsByType(String eventType);
    BaseResponse<CalendarResponse> confirmEvent(Long id, String user);
}
