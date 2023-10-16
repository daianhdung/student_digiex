package com.student_demo_digiex.service.imp;

import com.student_demo_digiex.common.exception.custom.APIRequestException;
import com.student_demo_digiex.common.utils.UniqueID;
import com.student_demo_digiex.dto.ClassDTO;
import com.student_demo_digiex.dto.StudentDTO;
import com.student_demo_digiex.dto.SubjectDTO;
import com.student_demo_digiex.dto.mapper.ClassMapper;
import com.student_demo_digiex.dto.mapper.SubjectMapper;
import com.student_demo_digiex.entity.ClassEntity;
import com.student_demo_digiex.model.request.FilterClassRequest;
import com.student_demo_digiex.model.response.PagingClassResponse;
import com.student_demo_digiex.repository.ClassRepository;
import com.student_demo_digiex.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.student_demo_digiex.dto.mapper.ClassMapper.dtoToEntity;
import static com.student_demo_digiex.dto.mapper.ClassMapper.entityToDTO;
import static com.student_demo_digiex.dto.mapper.StudentMapper.entityToDTO;

@Service
public class ClassServiceImp implements ClassService {

    @Autowired
    ClassRepository classRepository;

    @Override
    public ClassDTO getClassById(String idClass) {
        Optional<ClassEntity> classEntity = classRepository.findById(idClass);
        if(classEntity.isPresent()){
            ClassDTO classDTO = entityToDTO(classEntity.get());
            classDTO.setStudentCount(classRepository.countStudentInClass(classDTO.getId()));
            return classDTO;
        }else{
            throw new APIRequestException("Could not find the class");
        }
    }
    @Override
    public List<ClassDTO> getAllClass() {
        List<ClassEntity> listClassEntity = classRepository.findAll();

        List<ClassDTO> classDTOList = new ArrayList<>();

        listClassEntity.forEach(classEntity -> {

            ClassDTO classDTO = entityToDTO(classEntity);
            List<StudentDTO> studentDTOS = new ArrayList<>();

            classEntity.getStudentEntitySet().forEach(studentEntity -> {
                StudentDTO studentDTO = entityToDTO(studentEntity);

                List<SubjectDTO> subjectDTOList = studentEntity.getSubjectEntities()
                        .stream()
                        .map(SubjectMapper::entityToDTO).toList();
                studentDTO.setSubjectDTOS(subjectDTOList);
                studentDTOS.add(studentDTO);
            });

            classDTO.setStudentDTOList(studentDTOS);
            classDTOList.add(classDTO);
        });
        return classDTOList;
    }

    @Override
    public boolean createClass(ClassDTO classDTO) {
        if(classRepository.getClassEntitiesByName(classDTO.getName()) == null){
            classDTO.setId(UniqueID.getUUID());
            classRepository.save(dtoToEntity(classDTO));
            return true;
        }else{
            throw new APIRequestException("Class name already exists");
        }
    }

    @Override
    public boolean updateClass(ClassDTO classDTO) {
        if(classRepository.getClassEntitiesByName(classDTO.getName()) == null){
            Optional<ClassEntity> classEntity = classRepository.findById(classDTO.getId());
            if(classEntity.isPresent()){
                classEntity.get().setName(classDTO.getName());
                classEntity.get().setStatus(classDTO.getStatus());
                classEntity.get().setMaxStudent(classDTO.getMaxStudent());
            }else{
                throw new APIRequestException("Could not find the class");
            }
            classRepository.save(classEntity.get());
            return true;
        }else{
            throw new APIRequestException("Class name already exists");
        }
    }

    @Override
    public boolean deleteClass(String idClass) {
        Optional<ClassEntity> classEntity = classRepository.findById(idClass);
        if(classEntity.isPresent()){
            classRepository.delete(classEntity.get());
            return true;
        }else{
            throw new APIRequestException("Could not find the class");
        }
    }

    @Override
    public PagingClassResponse getClassByFilter(FilterClassRequest filterClassRequest) {
        int totalItem = filterClassRequest.getTotalItemEachPage();
        int currentPage = filterClassRequest.getCurrentPage() - 1;
        if(currentPage < 0){
            throw new APIRequestException("Page cannot less than 0");
        }

        Pageable pageable = PageRequest.of(currentPage, totalItem);

        String sortName = filterClassRequest.getSortNameType();
        String sortStudentCount = filterClassRequest.getSortStudentCount();
        if(sortName.equals("nameAsc")){
            pageable = PageRequest.of(currentPage, totalItem, Sort.by("name").ascending());
        } else if (sortName.equals("nameDesc")) {
            pageable = PageRequest.of(currentPage, totalItem, Sort.by("name").descending());
        }
        if(sortStudentCount.equals("maxStudentAsc")){
            pageable = PageRequest.of(currentPage, totalItem, Sort.by("maxStudent").ascending());
        } else if (sortStudentCount.equals("maxStudentDesc")) {
            pageable = PageRequest.of(currentPage, totalItem, Sort.by("maxStudent").descending());
        }

        Page<ClassEntity> page = classRepository
                .findByNameContainingIgnoreCase(filterClassRequest.getSearchKeyword()
                        , pageable);
        List<ClassDTO> classDTOList = page.getContent()
                .stream()
                .map(ClassMapper::entityToDTO).toList();

        PagingClassResponse pagingClassResponse = new PagingClassResponse();
        pagingClassResponse.setClassDTOS(classDTOList);
        pagingClassResponse.setCurrentPage(currentPage);
        pagingClassResponse.setTotalPage(page.getTotalPages());

        return pagingClassResponse;
    }

    public ClassEntity checkClassIfExists(String idClass) {
        Optional<ClassEntity> classEntity = classRepository.findById(idClass);
        if (classEntity.isPresent()) {
            return classEntity.get();
        } else {
            throw new APIRequestException("Could not find class");
        }
    }
}
