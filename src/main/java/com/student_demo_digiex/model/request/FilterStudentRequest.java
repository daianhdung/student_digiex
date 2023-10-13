package com.student_demo_digiex.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class FilterStudentRequest {

    private String classId;
    private String searchTerm;

    private String sortFirstName;
    private String sortLastName;
    private String sortEmail;
    private String sortDob;
    private String sortPhone;

    private String filterFirstName;
    private String filterLastName;
    private String filterEmail;
    private String filterGender;
    private String filterStartDate;
    private String filterEndDate;

    private int totalItemEachPage;
    private int currentPage;
}
