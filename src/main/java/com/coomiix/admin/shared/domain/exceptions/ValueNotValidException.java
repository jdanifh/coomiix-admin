package com.coomiix.admin.shared.domain.exceptions;

import lombok.Getter;

@Getter
public class ValueNotValidException extends RuntimeException {
    private String code = "VALUE_NOT_VALID";
    private String message;
    private String details;

    public ValueNotValidException(String message) {
        super(message);
        this.message = message;
    }

    public ValueNotValidException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    
    public ValueNotValidException(String code, String message, String details) {
        super(message);
        this.code = code;
        this.message = message;
        this.details = details;
    }
}
