package com.student_demo_digiex.dto.mapper;

import com.student_demo_digiex.dto.StudentDTO;
import com.student_demo_digiex.entity.StudentEntity;

import java.util.stream.Collectors;

public class StudentMapper {

    public static StudentDTO entityToDTO(StudentEntity studentEntity){
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(studentEntity.getId());
        studentDTO.setEmail(studentEntity.getEmail());
        studentDTO.setStatus(studentEntity.getStatus());
        studentDTO.setDob(studentEntity.getDob());
        studentDTO.setGender(studentEntity.getGender());
        studentDTO.setFirstName(studentEntity.getFirstName());
        studentDTO.setLastName(studentEntity.getLastName());
        studentDTO.setPhoneNumber(studentEntity.getPhoneNumber());
        studentDTO.setIdClass(studentEntity.getClassEntity().getId());
        studentDTO.setSubjectDTOS(studentEntity.getSubjectEntities().stream().map(SubjectMapper::entityToDTO).toList());
        return studentDTO;
    }

    public static StudentEntity dtoToEntity(StudentDTO studentDTO){
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(studentDTO.getId());
        studentEntity.setEmail(studentDTO.getEmail());
        studentEntity.setStatus(studentDTO.getStatus());
        studentEntity.setDob(studentDTO.getDob());
        studentEntity.setGender(studentDTO.getGender());
        studentEntity.setFirstName(studentDTO.getFirstName());
        studentEntity.setLastName(studentDTO.getLastName());
        studentEntity.setPhoneNumber(studentDTO.getPhoneNumber());
        studentEntity.setSubjectEntities(studentDTO.getSubjectDTOS().stream().map(SubjectMapper::dtoToEntity).collect(Collectors.toSet()));
        return studentEntity;
    }
}
