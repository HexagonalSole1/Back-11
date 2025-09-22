package org.devquality.privateproyect.calendar.infrastructure.controllers;

import org.devquality.privateproyect.calendar.application.services.ICalendarService;
import org.devquality.privateproyect.calendar.infrastructure.dtos.request.CalendarRequest;
import org.devquality.privateproyect.calendar.infrastructure.dtos.response.CalendarResponse;
import org.devquality.privateproyect.core.infrastructure.dtos.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    private final ICalendarService calendarService;

    @Autowired
    public CalendarController(ICalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @PostMapping("/events")
    public ResponseEntity<BaseResponse<CalendarResponse>> createEvent(@Valid @RequestBody CalendarRequest request) {
        BaseResponse<CalendarResponse> response = calendarService.createEvent(request);
        return response.toResponseEntity();
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<BaseResponse<CalendarResponse>> getEventById(@PathVariable Long id) {
        BaseResponse<CalendarResponse> response = calendarService.getEventById(id);
        return response.toResponseEntity();
    }

    @GetMapping("/events")
    public ResponseEntity<BaseResponse<List<CalendarResponse>>> getAllEvents() {
        BaseResponse<List<CalendarResponse>> response = calendarService.getAllEvents();
        return response.toResponseEntity();
    }

    @PutMapping("/events/{id}")
    public ResponseEntity<BaseResponse<CalendarResponse>> updateEvent(@PathVariable Long id, @Valid @RequestBody CalendarRequest request) {
        BaseResponse<CalendarResponse> response = calendarService.updateEvent(id, request);
        return response.toResponseEntity();
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<BaseResponse<Object>> deleteEvent(@PathVariable Long id) {
        BaseResponse<Object> response = calendarService.deleteEvent(id);
        return response.toResponseEntity();
    }

    @GetMapping("/events/type/{eventType}")
    public ResponseEntity<BaseResponse<List<CalendarResponse>>> getEventsByType(@PathVariable String eventType) {
        BaseResponse<List<CalendarResponse>> response = calendarService.getEventsByType(eventType);
        return response.toResponseEntity();
    }

    @PostMapping("/events/{id}/confirm/{user}")
    public ResponseEntity<BaseResponse<CalendarResponse>> confirmEvent(@PathVariable Long id, @PathVariable String user) {
        BaseResponse<CalendarResponse> response = calendarService.confirmEvent(id, user);
        return response.toResponseEntity();
    }
}
