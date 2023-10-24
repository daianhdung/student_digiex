package com.student_demo_digiex.dto.mapper;

import com.student_demo_digiex.common.enums.Status;
import com.student_demo_digiex.dto.ClassDTO;
import com.student_demo_digiex.entity.ClassEntity;

public class ClassMapper {

    public static ClassDTO entityToDTO(ClassEntity classEntity){
        ClassDTO classDTO = new ClassDTO();
        classDTO.setId(classEntity.getId());
        classDTO.setName(classEntity.getName());
        classDTO.setStatus(classEntity.getStatus());
        classDTO.setMaxStudent(classEntity.getMaxStudent());
        return classDTO;
    }

    public static ClassEntity dtoToEntity(ClassDTO classDTO){
        ClassEntity clasEntity = new ClassEntity();
        clasEntity.setId(classDTO.getId());
        clasEntity.setName(classDTO.getName());
        clasEntity.setStatus(classDTO.getStatus());
        clasEntity.setMaxStudent(classDTO.getMaxStudent());
        return clasEntity;
    }
}
