package com.prasun.BootCamp.ExceptionHandler;

public class PasswordMismatchException extends RuntimeException{
    public PasswordMismatchException(String message) {
        super(message);
    }
}
