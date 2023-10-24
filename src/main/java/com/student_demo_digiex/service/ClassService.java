package com.student_demo_digiex.service;

import com.student_demo_digiex.dto.ClassDTO;
import com.student_demo_digiex.entity.ClassEntity;
import com.student_demo_digiex.model.request.CreateClassRequest;
import com.student_demo_digiex.model.request.FilterClassRequest;
import com.student_demo_digiex.model.response.PagingClassResponse;

import java.util.List;

public interface ClassService {

    ClassDTO getClassById(String idClass);
    List<ClassDTO> getAllClass();
    ClassDTO createClass(CreateClassRequest createClass);

    ClassDTO updateClass(ClassDTO classDTO);

    void deleteClass(String idClass);

    PagingClassResponse getClassByFilter(FilterClassRequest filterClassRequest);

    ClassEntity checkClassIfExists(String idClass);
}
