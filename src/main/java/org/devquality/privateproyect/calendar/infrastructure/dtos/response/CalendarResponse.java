package org.devquality.privateproyect.calendar.infrastructure.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devquality.privateproyect.calendar.domain.enums.EventType;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalendarResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private EventType eventType;
    private Boolean isAllDay;
    private Boolean confirmedJulian;
    private Boolean confirmedDayani;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
