package com.prasun.BootCamp.ExceptionHandler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleALLException(Exception ex, WebRequest request) {
        List<String> list = new ArrayList<String>(Arrays.asList(request.getDescription(false)));
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,list);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public final ResponseEntity<Object> userNotFoundException(Exception ex, WebRequest request) {
        List<String> list = new ArrayList<String>(Arrays.asList(request.getDescription(false)));
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), HttpStatus.NOT_FOUND,list);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<String> details = new ArrayList<String>();
        details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ExceptionResponse exceptionResponse= new ExceptionResponse(
                new Date(),
                "Validation Errors",
                HttpStatus.BAD_REQUEST,
                details);


        return new ResponseEntity<>(exceptionResponse,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CustomerAlreadyExist.class)
    public final ResponseEntity<Object> customerAlreadyExist(CustomerAlreadyExist ex, WebRequest request){
        List<String> list = new ArrayList<String>(Arrays.asList(ex.getMessage(),request.getDescription(false)));
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "User already Exist", HttpStatus.BAD_REQUEST,list);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceExist.class)
    public final ResponseEntity<Object> resourceAlreadyExist(ResourceExist ex, WebRequest request){
        List<String> list = new ArrayList<String>(Arrays.asList(ex.getMessage(),request.getDescription(false)));
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Resource already Exist", HttpStatus.BAD_REQUEST,list);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceDoesNotExist.class)
    public final ResponseEntity<Object> resourceDoesNotExist(ResourceDoesNotExist ex, WebRequest request){
        List<String> list = new ArrayList<String>(Arrays.asList(ex.getMessage(),request.getDescription(false)));
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Resource does not Exist", HttpStatus.BAD_REQUEST,list);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}