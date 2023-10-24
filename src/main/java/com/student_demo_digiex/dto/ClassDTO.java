package com.student_demo_digiex.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.student_demo_digiex.common.enums.Status;
import com.student_demo_digiex.common.utils.Constant;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassDTO {

    private String id;
    private String name;
    private Integer maxStudent;

    private Status status;

    private Integer studentCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.API_FORMAT_DATE)
    private Date createdDate;

    List<StudentDTO> studentDTOList;

    public ClassDTO(String id, String name, Integer maxStudent) {
        this.id = id;
        this.name = name;
        this.maxStudent = maxStudent;
    }

    public ClassDTO(String id, String name, Integer maxStudent, Status status, Integer studentCount, Date createdDate, List<StudentDTO> studentDTOList) {
        this.id = id;
        this.name = name;
        this.maxStudent = maxStudent;
        this.status = status;
        this.studentCount = studentCount;
        this.createdDate = createdDate;
        this.studentDTOList = studentDTOList;
    }

    public ClassDTO(){}
}
