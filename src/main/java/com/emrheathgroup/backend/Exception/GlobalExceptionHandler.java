package com.emrheathgroup.backend.Exception;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.emrheathgroup.backend.DTOs.ResponseDTO;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDTO> handleException(Exception e) {
        ResponseDTO response = new ResponseDTO();
        response.setStatus(false);
        response.setMessage(e.getMessage());
        response.setData(new ArrayList<>());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleUserNotFound(Exception e) {
        ResponseDTO response = new ResponseDTO();
        response.setStatus(false);
        response.setMessage(e.getMessage());
        response.setData(new ArrayList<>());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ResponseDTO> handleInvalidCredentialsException(Exception e) {
        ResponseDTO response = new ResponseDTO();
        response.setStatus(false);
        response.setMessage(e.getMessage());
        response.setData(new ArrayList<>());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

}
