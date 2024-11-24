package org.lukas.decision.model;

import java.text.ParseException;

public enum Importance {
    MINOR,
    MAJOR,
    CRITICAL;

    public static Importance fromString(String s) throws ParseException {
        return switch (s) {
            case "minor" -> Importance.MINOR;
            case "major" -> Importance.MAJOR;
            case "critical" -> Importance.CRITICAL;
            default -> throw new ParseException("No such importance level", 0);
        };
    }
}
