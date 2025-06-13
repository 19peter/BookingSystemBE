package com.pd.BookingSystem.exceptions.CustomExceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String err) {
        super(err);
    }
}
