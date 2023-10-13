package com.student_demo_digiex.common.exception.custom;


public class APIRequestException extends RuntimeException{

    public APIRequestException(String message) {
        super(message);
    }
}
