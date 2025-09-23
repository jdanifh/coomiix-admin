package com.coomiix.admin.player.domain.valueobjects;

import com.coomiix.admin.shared.domain.exceptions.ValueNotValidException;

public record Email(String value) {

    public Email {
        if (value == null || value.isBlank()) {
            throw new ValueNotValidException("Email cannot be null or empty");
        }
        if (!isValid(value)) {
            throw new ValueNotValidException("Invalid email address");
        }
    }

    public static boolean isValid(String value) {
        return value != null && value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

}
