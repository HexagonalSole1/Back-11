package org.devquality.privateproyect.calendar.infrastructure.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class CalendarRequest {

    @NotBlank(message = "El título del evento es obligatorio")
    @Size(max = 100, message = "El título no puede exceder 100 caracteres")
    private String title;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String description;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime startDate;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDateTime endDate;

    @NotNull(message = "El tipo de evento es obligatorio")
    private EventType eventType;

    @Builder.Default
    private Boolean isAllDay = false;

    @Builder.Default
    private Boolean confirmedJulian = false;

    @Builder.Default
    private Boolean confirmedDayani = false;
}
