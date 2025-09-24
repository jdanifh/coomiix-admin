package com.coomiix.admin.shared.domain.exceptions;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private String code = "RESOURCE_NOT_FOUND";
    private String message;
    private String details;

    public ResourceNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public ResourceNotFoundException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    
    public ResourceNotFoundException(String code, String message, String details) {
        super(message);
        this.code = code;
        this.message = message;
        this.details = details;
    }

}
