package com.student_demo_digiex.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ClassDTO {

    @NotNull(message = "Id is mandatory")
    private String id;

    @NotNull(message = "Name is mandatory")
    private String name;

    @Max(value = 20, message = "Class can't have more than 20 students")
    @NotNull(message = "Max Student is mandatory")
    private int maxStudent;

    @NotNull(message = "Status is mandatory")
    private String status;

    private int studentCount;

    List<StudentDTO> studentDTOList;

}
