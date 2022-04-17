package com.prasun.BootCamp.ExceptionHandler;
public class ResourceExist extends RuntimeException{
    public ResourceExist(String message) {
        super(message);
    }
}