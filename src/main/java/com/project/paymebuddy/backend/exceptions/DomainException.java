package com.project.paymebuddy.backend.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.project.paymebuddy.backend.utils.Constants.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class DomainException extends RuntimeException {

    final Severity severity;
    final Code code;
    final String text;

    public DomainException(Severity severity, Code code, String text) {
        super(text);
        this.severity = severity;
        this.code = code;
        this.text = text;
    }

    public enum Severity {
        FATAL("Fatal"),
        TRANSIENT("Transient"),
        LOGIC("Logic");

        private final String text;

        Severity(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    public enum Code {
        NOT_FOUND("NOT_FOUND", CODE_404),
        BAD_REQUEST("BAD_REQUEST", CODE_400),
        UNAUTHORIZED("UNAUTHORIZED", CODE_401),
        FORBIDDEN("FORBIDDEN", CODE_403),
        INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", CODE_500);

        private final String text;
        private final int value;

        Code(String text, int value) {
            this.text = text;
            this.value = value;
        }

        public String getText() {
            return text;
        }

        public int getValue() {
            return value;
        }
    }
}
