package com.farmapp.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FarmAppException extends RuntimeException {
    private final HttpStatus status;

    public FarmAppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
