package com.pd.BookingSystem.exceptions.CustomExceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String err) {
        super(err);
    }
}
