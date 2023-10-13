package com.student_demo_digiex.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterClassRequest {

    private int currentPage;

    private String searchKeyword;

    private String sortNameType;

    private String sortStudentCount;

    private int totalItemEachPage;
}
