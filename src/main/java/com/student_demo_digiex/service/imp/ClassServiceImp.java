package com.student_demo_digiex.service.imp;

import com.student_demo_digiex.common.enums.Status;
import com.student_demo_digiex.common.exception.custom.APIRequestException;
import com.student_demo_digiex.common.utils.UniqueID;
import com.student_demo_digiex.dto.ClassDTO;
import com.student_demo_digiex.dto.StudentDTO;
import com.student_demo_digiex.dto.SubjectDTO;
import com.student_demo_digiex.dto.mapper.ClassMapper;
import com.student_demo_digiex.dto.mapper.SubjectMapper;
import com.student_demo_digiex.entity.ClassEntity;
import com.student_demo_digiex.entity.StudentEntity;
import com.student_demo_digiex.entity.SubjectEntity;
import com.student_demo_digiex.model.request.CreateClassRequest;
import com.student_demo_digiex.model.request.FilterClassRequest;
import com.student_demo_digiex.model.response.PagingClassResponse;
import com.student_demo_digiex.repository.ClassRepository;
import com.student_demo_digiex.repository.StudentRepository;
import com.student_demo_digiex.repository.SubjectRepository;
import com.student_demo_digiex.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.student_demo_digiex.dto.mapper.ClassMapper.entityToDTO;
import static com.student_demo_digiex.dto.mapper.StudentMapper.entityToDTO;

@Service
public class ClassServiceImp implements ClassService {

    @Autowired
    ClassRepository classRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Override
    public ClassDTO getClassById(String idClass) {
        ClassEntity classEntity = checkClassIfExists(idClass);
        ClassDTO classDTO = entityToDTO(classEntity);
        classDTO.setStudentCount(classRepository.countStudentInClass(classDTO.getId()));
        return classDTO;
    }
    @Override
    public List<ClassDTO> getAllClass() {
        List<ClassEntity> listClassEntity = classRepository.findAll();
        List<StudentEntity> studentEntityList = studentRepository.findAll();
        List<SubjectEntity> subjectEntityList = subjectRepository.findAll();

        List<ClassDTO> classDTOList = new ArrayList<>();
        listClassEntity.forEach(classEntity -> {
            ClassDTO classDTO = entityToDTO(classEntity);
            classDTO.setStudentCount(classRepository.countStudentInClass(classDTO.getId()));
            List<StudentDTO> studentDTOS = new ArrayList<>();

            studentEntityList.stream().filter(item -> item.getClassId().equals(classEntity.getId())).forEach(studentEntity -> {
                StudentDTO studentDTO = entityToDTO(studentEntity);
                List<SubjectDTO> subjectDTOList = subjectEntityList
                        .stream()
                        .filter(item -> item.getStudentId().equals(studentDTO.getId()))
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
    public ClassDTO createClass(CreateClassRequest createClass) {
        ClassEntity nameExisted = classRepository.getClassEntitiesByName(createClass.getName().trim());
        if (nameExisted != null){
            throw new APIRequestException("Class name already exists");
        }
        ClassEntity classEntity = new ClassEntity();
        classEntity.setId(UniqueID.getUUID());
        classEntity.setStatus(Status.ACTIVE);
        classEntity.setMaxStudent(createClass.getMaxStudent());
        classEntity.setName(createClass.getName().trim());
        classRepository.save(classEntity);
        return entityToDTO(classEntity);
    }

    @Override
    public ClassDTO updateClass(ClassDTO classDTO) {
        ClassEntity classEntity = checkClassIfExists(classDTO.getId());
        if (classEntity == null){
            throw new APIRequestException("Class not found");
        }

        if (classDTO.getName() != null && !classDTO.getName().trim().isEmpty()){
            ClassEntity nameExisted = classRepository.getClassEntitiesByName(classDTO.getName().trim());
            if (nameExisted != null && !classEntity.getId().equals(nameExisted.getId())){
                throw new APIRequestException("Class name already exists");
            }
            classEntity.setName(classEntity.getName().trim());
        }
        if (classDTO.getMaxStudent() != null){
            classEntity.setMaxStudent(classDTO.getMaxStudent());
        }
        classRepository.save(classEntity);
        return entityToDTO(classEntity);
    }

    @Override
    public void deleteClass(String idClass) {
        classRepository.delete(checkClassIfExists(idClass));
        studentRepository.deleteAllByClassId(idClass);
        List<String> studentIds = studentRepository.findAllIdStudentByClassId(idClass);
        subjectRepository.deleteAllByStudentIdIsIn(studentIds);
    }

    @Override
    public PagingClassResponse getClassByFilter(FilterClassRequest filterClassRequest) {
        int totalItem = filterClassRequest.getTotalItemEachPage();
        int currentPage = filterClassRequest.getCurrentPage() - 1;
        if(currentPage < 0){
            throw new APIRequestException("Page cannot less than 0");
        }

        Pageable pageable = PageRequest.of(currentPage, totalItem);

        if(filterClassRequest.getSortType().equals("asc")){
            pageable = PageRequest.of(currentPage, totalItem, Sort.by(filterClassRequest.getSortField()).ascending());
        } else if (filterClassRequest.getSortType().equals("desc")) {
            pageable = PageRequest.of(currentPage, totalItem, Sort.by(filterClassRequest.getSortField()).descending());
        }

        Page<ClassEntity> page = classRepository
                .findByNameContainingIgnoreCase(filterClassRequest.getSearchKeyword().trim()
                        , pageable);

        List<ClassDTO> classDTOList = page.getContent()
                .stream()
                .map(ClassMapper::entityToDTO).toList();

        PagingClassResponse pagingClassResponse = new PagingClassResponse();
        pagingClassResponse.setClassDTOS(classDTOList);
        pagingClassResponse.setCurrentPage(filterClassRequest.getCurrentPage());
        pagingClassResponse.setTotalPage(page.getTotalPages());

        return pagingClassResponse;
    }

    public ClassEntity checkClassIfExists(String idClass) {
        ClassEntity classEntity = classRepository.findByIdAndStatus(idClass, Status.ACTIVE);
        if (classEntity != null) {
            return classEntity;
        } else {
            throw new APIRequestException("Could not find class");
        }
    }
}
