package com.pd.BookingSystem.exceptions.CustomExceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String err) {
        super(err);
    }
}
