package org.devquality.privateproyect.calendar.domain.enums;

public enum EventType {
    IMPORTANT("Importante"),
    MEETING("Reunión"),
    DEADLINE("Fecha límite"),
    REMINDER("Recordatorio"),
    HOLIDAY("Día festivo"),
    PERSONAL("Personal");

    private final String displayName;

    EventType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
