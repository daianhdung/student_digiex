package com.student_demo_digiex.model.request;

import com.student_demo_digiex.common.utils.RegexPattern;
import lombok.*;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FilterStudentRequest {

    private String classId;
    private String searchTerm;

    private String sortField;
    private String sortType;

    private String filterGender;

    @Pattern(regexp = RegexPattern.patternDate, message = "Not a valid date")
    private String filterStartDate;

    @Pattern(regexp = RegexPattern.patternDate, message = "Not a valid date")
    private String filterEndDate;

    private Integer totalItemEachPage;
    private Integer currentPage;
}
