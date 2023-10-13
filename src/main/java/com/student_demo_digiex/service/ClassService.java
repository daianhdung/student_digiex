package com.student_demo_digiex.service;

import com.student_demo_digiex.dto.ClassDTO;
import com.student_demo_digiex.entity.ClassEntity;
import com.student_demo_digiex.model.request.FilterClassRequest;
import com.student_demo_digiex.model.response.PagingClassResponse;

import java.util.List;

public interface ClassService {

    ClassDTO getClassById(String idClass);

    List<ClassDTO> getAllClass();
    boolean createClass(ClassDTO classDTO);

    boolean updateClass(ClassDTO classDTO);

    boolean deleteClass(String idClass);

    PagingClassResponse getClassByFilter(FilterClassRequest filterClassRequest);

    ClassEntity checkClassIfExists(String idClass);
}
