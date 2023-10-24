package com.student_demo_digiex.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.student_demo_digiex.common.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubjectDTO {

    private String id;

    private String name;

    private Double score;

    private Integer numberOfLessons;

    private Status status;

    private String idStudent;
}
