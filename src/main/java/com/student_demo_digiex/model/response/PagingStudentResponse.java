package com.student_demo_digiex.model.response;

import com.student_demo_digiex.dto.StudentDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagingStudentResponse {

    private List<StudentDTO> studentDTOList;
    private int currentPage;
    private int totalPages;
}
