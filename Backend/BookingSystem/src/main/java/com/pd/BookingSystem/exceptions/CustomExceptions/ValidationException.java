package com.pd.BookingSystem.exceptions.CustomExceptions;

public class ValidationException extends RuntimeException{

    public ValidationException(String err) {
        super(err);
    }

}
