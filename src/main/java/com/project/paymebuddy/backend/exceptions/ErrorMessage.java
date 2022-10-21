package com.project.paymebuddy.backend.exceptions;

import java.time.LocalDateTime;

public record ErrorMessage(String severity, int statusCode, LocalDateTime timestamp, String message, String description) {
}
