package com.student_demo_digiex.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateClassRequest {
    private String name;
    private Integer maxStudent;
}
