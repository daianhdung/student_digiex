package com.student_demo_digiex.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdateClassRequest {
    private String name;

    @Size(max = 20, message = "Cannot over 20")
    private Integer maxStudent;
}
