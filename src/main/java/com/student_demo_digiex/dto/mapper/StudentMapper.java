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
        studentDTO.setIdClass(studentEntity.getClassId());
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
        return studentEntity;
    }

    public static StudentEntity dtoToEntityUpdate(StudentEntity studentEntity, StudentDTO studentDTO){
        if(studentDTO.getEmail() != null && !studentDTO.getEmail().trim().isEmpty()){
            studentEntity.setEmail(studentDTO.getEmail());
        }
        if(studentDTO.getDob() != null){
            studentEntity.setDob(studentDTO.getDob());
        }
        if(studentDTO.getGender() != null && !studentDTO.getGender().trim().isEmpty()){
            studentEntity.setEmail(studentDTO.getGender());
        }
        if(studentDTO.getFirstName() != null && !studentDTO.getFirstName().trim().isEmpty()){
            studentEntity.setEmail(studentDTO.getFirstName());
        }

        if(studentDTO.getLastName() != null && !studentDTO.getLastName().trim().isEmpty()){
            studentEntity.setEmail(studentDTO.getLastName());
        }

        if(studentDTO.getPhoneNumber() != null && !studentDTO.getPhoneNumber().trim().isEmpty()){
            studentEntity.setEmail(studentDTO.getPhoneNumber());
        }
        return studentEntity;
    }
}
