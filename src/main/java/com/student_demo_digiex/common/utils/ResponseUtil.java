package com.student_demo_digiex.common.utils;

import com.student_demo_digiex.model.response.DataResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtil {

    private DataResponse createResponse(RestAPIStatus restAPIStatus, Object data){
        return new DataResponse(restAPIStatus, data);
    }

    private DataResponse createResponse(RestAPIStatus restAPIStatus, Object data, String description){
        return new DataResponse(restAPIStatus, data, description);
    }

    public ResponseEntity<DataResponse> buildResponse(RestAPIStatus restAPIStatus, Object data, HttpStatus httpStatus){
        return new ResponseEntity<>(createResponse(restAPIStatus, data), httpStatus);
    }

    public ResponseEntity<DataResponse> buildResponse(RestAPIStatus restAPIStatus, Object data, String description, HttpStatus httpStatus){
        return new ResponseEntity<>(createResponse(restAPIStatus, data, description), httpStatus);
    }
}
