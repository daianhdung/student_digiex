package com.student_demo_digiex.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FilterClassRequest {

    private int currentPage;

    private String searchKeyword;

    private String sortField;

    private String sortType;

    private int totalItemEachPage;
}
