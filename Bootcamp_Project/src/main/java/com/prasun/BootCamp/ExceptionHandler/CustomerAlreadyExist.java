package com.prasun.BootCamp.ExceptionHandler;

public class CustomerAlreadyExist extends RuntimeException {

    public CustomerAlreadyExist(String message)
    {
        super(message);
    }
}