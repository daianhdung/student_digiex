package com.student_demo_digiex.common.exception;

import com.student_demo_digiex.common.exception.custom.APIRequestException;
import com.student_demo_digiex.model.response.DataResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class APIHandlerException{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> {
            errorMap.put(err.getField(), err.getDefaultMessage());
        });
        return errorMap;
    }

    @ExceptionHandler(APIRequestException.class)
    public ResponseEntity<?> handleAPIErr(APIRequestException ex) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setDesc(ex.getMessage());
        dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(dataResponse, HttpStatus.BAD_REQUEST);
    }
}
