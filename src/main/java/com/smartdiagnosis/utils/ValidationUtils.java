package com.smartdiagnosis.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * ValidationUtils - Input validation helpers used across the application.
 * Demonstrates: Static utility methods, exception handling
 */
public class ValidationUtils {

    public static boolean isValidAge(int age) {
        return age >= 1 && age <= 120;
    }

    public static boolean isValidSeverity(int severity) {
        return severity >= 1 && severity <= 5;
    }

    public static boolean isNonEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidHeight(double height) {
        return height >= 50 && height <= 300; // cm
    }

    public static boolean isValidWeight(double weight) {
        return weight >= 1 && weight <= 500; // kg
    }

    /** Remove potentially dangerous characters from user input */
    public static String sanitizeInput(String input) {
        if (input == null) return "";
        return input.trim().replaceAll("[<>\"'%;()&+\\\\]", "");
    }

    public static boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static String getWellnessLabel(int score) {
        if (score >= 85) return "Excellent";
        if (score >= 70) return "Good";
        if (score >= 55) return "Fair";
        if (score >= 40) return "Below Average";
        return "Needs Attention";
    }

    public static String getSeverityLabel(int severity) {
        switch (severity) {
            case 1: return "Very Mild";
            case 2: return "Mild";
            case 3: return "Moderate";
            case 4: return "Severe";
            case 5: return "Very Severe";
            default: return "Unknown";
        }
    }

    public static String getSeverityEmoji(int severity) {
        switch (severity) {
            case 1: return "\uD83D\uDFE2"; // green circle
            case 2: return "\uD83D\uDFE1"; // yellow
            case 3: return "\uD83D\uDFE0"; // orange
            case 4: return "\uD83D\uDD34"; // red
            case 5: return "\uD83D\uDD25"; // fire
            default: return "\u26AA";
        }
    }

    public static String getCompatibilityColor(String label) {
        switch (label) {
            case "HIGH":     return "#e74c3c";
            case "MODERATE": return "#f39c12";
            case "LOW":      return "#27ae60";
            default:         return "#95a5a6";
        }
    }

    /** Format a LocalDate for display */
    public static String formatDate(LocalDate date) {
        if (date == null) return "N/A";
        return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    /** Truncate a string to max length with ellipsis */
    public static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }
}
