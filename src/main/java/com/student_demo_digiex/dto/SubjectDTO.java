package com.student_demo_digiex.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SubjectDTO {

    @NotNull(message = "Id is mandatory")
    private String id;

    @NotNull(message = "Name is mandatory")
    private String name;

    @Max(value = 10, message = "Cannot higher than 10")
    @Min(value = 0, message = "Cannot lower than 0")
    private double score;

    @NotNull(message = "Number of Lessons is mandatory")
    private int numberOfLessons;

    private String status;

    private String idStudent;
}
