package com.student_demo_digiex.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataResponse {

    private int status;
    private String desc;
    private Object data;
}
