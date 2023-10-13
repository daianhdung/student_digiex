package com.student_demo_digiex.model.response;

import com.student_demo_digiex.dto.ClassDTO;
import com.student_demo_digiex.entity.ClassEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagingClassResponse {

    private List<ClassDTO> classDTOS;
    private int totalPage;
    private int currentPage;
}
