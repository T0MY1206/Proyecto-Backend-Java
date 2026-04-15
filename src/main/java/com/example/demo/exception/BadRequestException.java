package com.example.demo.exception;

public class BadRequestException extends BusinessException {

    public BadRequestException(String message) {
        super(message);
    }
}
