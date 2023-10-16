package com.student_demo_digiex.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.student_demo_digiex.common.utils.RestAPIStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataResponse<T extends Object> implements Serializable {

    public DataResponse(){}

    public DataResponse(RestAPIStatus restApiStatus, T data) {

        if (restApiStatus == null) {
            throw new IllegalArgumentException("APIStatus must not be null");
        }

        this.status = restApiStatus.getCode();
        this.message = restApiStatus.getDescription();
        this.data = data;
        this.desc = "";
    }

    public DataResponse(RestAPIStatus restApiStatus, T data, String description) {

        if (restApiStatus == null) {
            throw new IllegalArgumentException("APIStatus must not be null");
        }

        this.status = restApiStatus.getCode();
        this.message = restApiStatus.getDescription();
        this.data = data;
        this.desc = description;
    }

    private int status;
    private String desc;
    private Object data;
    private String message;
}
