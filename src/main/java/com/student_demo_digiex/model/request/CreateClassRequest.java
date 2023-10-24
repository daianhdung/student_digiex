package com.student_demo_digiex.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateClassRequest {


    @NotBlank(message = "Name is mandatory")
    private String name;

    @Max(value = 20, message = "Class can't have more than 20 students")
    @NotNull(message = "Max Student is mandatory")
    private Integer maxStudent;

}
