package com.pd.BookingSystem.exceptions.CustomExceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String err) {
        super(err);
    }
}
