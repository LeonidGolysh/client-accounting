package com.ua.client_accounting.car.exception;

public class CarNotFoundException extends RuntimeException{
    public CarNotFoundException(String message) {
        super(message);
    }
}
