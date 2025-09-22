package org.devquality.privateproyect.calendar.application.services.impl;

import org.devquality.privateproyect.calendar.application.mappers.CalendarMapper;
import org.devquality.privateproyect.calendar.application.services.ICalendarService;
import org.devquality.privateproyect.calendar.domain.entities.Calendar;
import org.devquality.privateproyect.calendar.domain.enums.EventType;
import org.devquality.privateproyect.calendar.domain.repositories.CalendarRepository;
import org.devquality.privateproyect.calendar.infrastructure.dtos.request.CalendarRequest;
import org.devquality.privateproyect.calendar.infrastructure.dtos.response.CalendarResponse;
import org.devquality.privateproyect.core.infrastructure.dtos.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ICalendarServiceImpl implements ICalendarService {

    private final CalendarRepository calendarRepository;
    private final CalendarMapper calendarMapper;

    @Autowired
    public ICalendarServiceImpl(CalendarRepository calendarRepository, CalendarMapper calendarMapper) {
        this.calendarRepository = calendarRepository;
        this.calendarMapper = calendarMapper;
    }

    @Override
    public BaseResponse<CalendarResponse> createEvent(CalendarRequest request) {
        try {
            Calendar calendar = calendarMapper.toEntity(request);
            Calendar savedCalendar = calendarRepository.save(calendar);
            CalendarResponse response = calendarMapper.toResponse(savedCalendar);
            
            return BaseResponse.<CalendarResponse>builder()
                    .message("Evento creado exitosamente")
                    .data(response)
                    .status(org.springframework.http.HttpStatus.CREATED)
                    .success(true)
                    .build();
        } catch (Exception e) {
            return BaseResponse.<CalendarResponse>builder()
                    .message("Error al crear el evento: " + e.getMessage())
                    .status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                    .success(false)
                    .build();
        }
    }

    @Override
    public BaseResponse<CalendarResponse> getEventById(Long id) {
        try {
            Optional<Calendar> calendar = calendarRepository.findById(id);
            if (calendar.isPresent()) {
                CalendarResponse response = calendarMapper.toResponse(calendar.get());
                return BaseResponse.<CalendarResponse>builder()
                        .message("Evento encontrado")
                        .data(response)
                        .status(org.springframework.http.HttpStatus.OK)
                        .success(true)
                        .build();
            } else {
                return BaseResponse.<CalendarResponse>builder()
                        .message("Evento no encontrado")
                        .status(org.springframework.http.HttpStatus.NOT_FOUND)
                        .success(false)
                        .build();
            }
        } catch (Exception e) {
            return BaseResponse.<CalendarResponse>builder()
                    .message("Error al buscar el evento: " + e.getMessage())
                    .status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                    .success(false)
                    .build();
        }
    }

    @Override
    public BaseResponse<List<CalendarResponse>> getAllEvents() {
        try {
            List<Calendar> calendars = calendarRepository.findAll();
            List<CalendarResponse> responses = calendars.stream()
                    .map(calendarMapper::toResponse)
                    .collect(Collectors.toList());
            
            return BaseResponse.<List<CalendarResponse>>builder()
                    .message("Eventos obtenidos exitosamente")
                    .data(responses)
                    .status(org.springframework.http.HttpStatus.OK)
                    .success(true)
                    .build();
        } catch (Exception e) {
            return BaseResponse.<List<CalendarResponse>>builder()
                    .message("Error al obtener los eventos: " + e.getMessage())
                    .status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                    .success(false)
                    .build();
        }
    }

    @Override
    public BaseResponse<CalendarResponse> updateEvent(Long id, CalendarRequest request) {
        try {
            Optional<Calendar> calendarOpt = calendarRepository.findById(id);
            if (calendarOpt.isPresent()) {
                Calendar calendar = calendarOpt.get();
                calendarMapper.updateEntity(calendar, request);
                Calendar updatedCalendar = calendarRepository.save(calendar);
                CalendarResponse response = calendarMapper.toResponse(updatedCalendar);
                
                return BaseResponse.<CalendarResponse>builder()
                        .message("Evento actualizado exitosamente")
                        .data(response)
                        .status(org.springframework.http.HttpStatus.OK)
                        .success(true)
                        .build();
            } else {
                return BaseResponse.<CalendarResponse>builder()
                        .message("Evento no encontrado")
                        .status(org.springframework.http.HttpStatus.NOT_FOUND)
                        .success(false)
                        .build();
            }
        } catch (Exception e) {
            return BaseResponse.<CalendarResponse>builder()
                    .message("Error al actualizar el evento: " + e.getMessage())
                    .status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                    .success(false)
                    .build();
        }
    }

    @Override
    public BaseResponse<Object> deleteEvent(Long id) {
        try {
            Optional<Calendar> calendar = calendarRepository.findById(id);
            if (calendar.isPresent()) {
                calendarRepository.deleteById(id);
                return BaseResponse.<Object>builder()
                        .message("Evento eliminado exitosamente")
                        .status(org.springframework.http.HttpStatus.OK)
                        .success(true)
                        .build();
            } else {
                return BaseResponse.<Object>builder()
                        .message("Evento no encontrado")
                        .status(org.springframework.http.HttpStatus.NOT_FOUND)
                        .success(false)
                        .build();
            }
        } catch (Exception e) {
            return BaseResponse.<Object>builder()
                    .message("Error al eliminar el evento: " + e.getMessage())
                    .status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                    .success(false)
                    .build();
        }
    }

    @Override
    public BaseResponse<List<CalendarResponse>> getEventsByType(String eventType) {
        try {
            EventType type = EventType.valueOf(eventType.toUpperCase());
            List<Calendar> calendars = calendarRepository.findByEventType(type);
            List<CalendarResponse> responses = calendars.stream()
                    .map(calendarMapper::toResponse)
                    .collect(Collectors.toList());
            
            return BaseResponse.<List<CalendarResponse>>builder()
                    .message("Eventos obtenidos por tipo exitosamente")
                    .data(responses)
                    .status(org.springframework.http.HttpStatus.OK)
                    .success(true)
                    .build();
        } catch (IllegalArgumentException e) {
            return BaseResponse.<List<CalendarResponse>>builder()
                    .message("Tipo de evento inválido: " + eventType)
                    .status(org.springframework.http.HttpStatus.BAD_REQUEST)
                    .success(false)
                    .build();
        } catch (Exception e) {
            return BaseResponse.<List<CalendarResponse>>builder()
                    .message("Error al obtener los eventos por tipo: " + e.getMessage())
                    .status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                    .success(false)
                    .build();
        }
    }

    @Override
    public BaseResponse<CalendarResponse> confirmEvent(Long id, String user) {
        try {
            Optional<Calendar> calendarOpt = calendarRepository.findById(id);
            if (calendarOpt.isPresent()) {
                Calendar calendar = calendarOpt.get();
                
                if ("julian".equalsIgnoreCase(user)) {
                    calendar.setConfirmedJulian(true);
                } else if ("dayani".equalsIgnoreCase(user)) {
                    calendar.setConfirmedDayani(true);
                } else {
                    return BaseResponse.<CalendarResponse>builder()
                            .message("Usuario inválido. Use 'julian' o 'dayani'")
                            .status(org.springframework.http.HttpStatus.BAD_REQUEST)
                            .success(false)
                            .build();
                }
                
                Calendar updatedCalendar = calendarRepository.save(calendar);
                CalendarResponse response = calendarMapper.toResponse(updatedCalendar);
                
                return BaseResponse.<CalendarResponse>builder()
                        .message("Evento confirmado exitosamente por " + user)
                        .data(response)
                        .status(org.springframework.http.HttpStatus.OK)
                        .success(true)
                        .build();
            } else {
                return BaseResponse.<CalendarResponse>builder()
                        .message("Evento no encontrado")
                        .status(org.springframework.http.HttpStatus.NOT_FOUND)
                        .success(false)
                        .build();
            }
        } catch (Exception e) {
            return BaseResponse.<CalendarResponse>builder()
                    .message("Error al confirmar el evento: " + e.getMessage())
                    .status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                    .success(false)
                    .build();
        }
    }
}
