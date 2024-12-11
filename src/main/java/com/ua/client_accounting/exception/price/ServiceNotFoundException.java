package com.ua.client_accounting.exception.price;

public class ServiceNotFoundException extends RuntimeException{
    public ServiceNotFoundException(String message) {
        super(message);
    }
}
