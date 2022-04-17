package com.prasun.BootCamp.ExceptionHandler;

public class ResourceDoesNotExist extends RuntimeException{
    public ResourceDoesNotExist(String message) {
        super(message);
    }
}
