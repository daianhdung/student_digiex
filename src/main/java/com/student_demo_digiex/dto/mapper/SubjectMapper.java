package com.student_demo_digiex.dto.mapper;

import com.student_demo_digiex.dto.SubjectDTO;
import com.student_demo_digiex.entity.SubjectEntity;

public class SubjectMapper {

    public static SubjectDTO entityToDTO(SubjectEntity subjectEntity){
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setId(subjectEntity.getId());
        subjectDTO.setName(subjectEntity.getName());
        subjectDTO.setStatus(subjectEntity.getStatus());
        subjectDTO.setScore(subjectEntity.getScore());
        subjectDTO.setNumberOfLessons(subjectEntity.getNumberOfLessons());
        subjectDTO.setIdStudent(subjectEntity.getStudentEntity().getId());
        return subjectDTO;
    }

    public static SubjectEntity dtoToEntity(SubjectDTO subjectDTO){
        SubjectEntity subjectEntity = new SubjectEntity();
        subjectEntity.setId(subjectDTO.getId());
        subjectEntity.setName(subjectDTO.getName());
        subjectEntity.setStatus(subjectDTO.getStatus());
        subjectEntity.setScore(subjectDTO.getScore());
        subjectEntity.setNumberOfLessons(subjectDTO.getNumberOfLessons());
        return subjectEntity;
    }
}
