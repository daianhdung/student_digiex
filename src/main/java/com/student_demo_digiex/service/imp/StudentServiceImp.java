package com.student_demo_digiex.service.imp;

import com.student_demo_digiex.common.exception.custom.APIRequestException;
import com.student_demo_digiex.dto.ClassDTO;
import com.student_demo_digiex.dto.StudentDTO;
import com.student_demo_digiex.dto.SubjectDTO;
import com.student_demo_digiex.dto.mapper.ClassMapper;
import com.student_demo_digiex.dto.mapper.StudentMapper;
import com.student_demo_digiex.entity.ClassEntity;
import com.student_demo_digiex.entity.StudentEntity;
import com.student_demo_digiex.entity.SubjectEntity;
import com.student_demo_digiex.model.request.FilterStudentRequest;
import com.student_demo_digiex.model.response.PagingStudentResponse;
import com.student_demo_digiex.repository.ClassRepository;
import com.student_demo_digiex.repository.StudentRepository;
import com.student_demo_digiex.repository.SubjectRepository;
import com.student_demo_digiex.service.ClassService;
import com.student_demo_digiex.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.student_demo_digiex.dto.mapper.StudentMapper.dtoToEntity;
import static com.student_demo_digiex.dto.mapper.StudentMapper.entityToDTO;
import static com.student_demo_digiex.dto.mapper.SubjectMapper.dtoToEntity;
import static com.student_demo_digiex.dto.mapper.SubjectMapper.entityToDTO;
import static java.lang.Double.compare;

@Service
public class StudentServiceImp implements StudentService {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    ClassRepository classRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    ClassService classService;

    @Override
    public List<StudentDTO> getAllStudentByClassIdDefaultSortHighScore(String classId) {
        classService.checkClassIfExists(classId);

        List<StudentDTO> studentDTOList = getAllStudentByClassId(classId);
        return studentDTOList
                .stream()
                .sorted((o2, o1) -> compare(o1.getAverageScore(), o2.getAverageScore()))
                .collect(Collectors.toList());
    }

    @Override
    public StudentDTO getStudentById(String idStudent) {
        StudentEntity studentEntity = checkStudentIfExists(idStudent);
        StudentDTO studentDTO = entityToDTO(studentEntity);
        double averageScore = 0;
        for (var item : studentEntity.getSubjectEntities()) {
            averageScore += item.getScore();
        }
        averageScore = averageScore / studentEntity.getSubjectEntities().size();
        studentDTO.setAverageScore(averageScore);
        return studentDTO;
    }

    @Override
    public PagingStudentResponse pagingStudent(FilterStudentRequest filterStudentRequest) {
        int totalItem = filterStudentRequest.getTotalItemEachPage();
        int currentPage = filterStudentRequest.getCurrentPage() - 1;
        if (currentPage < 0) {
            throw new APIRequestException("Page cannot less than 0");
        }
        Pageable pageable = PageRequest.of(currentPage, totalItem);

        if (filterStudentRequest.getFilterStartDate().isEmpty()) {
            filterStudentRequest.setFilterStartDate("1900-01-01");
        }
        //Default endDate now()
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Định dạng của chuỗi đầu ra
        String dateStr = sdf.format(currentDate);
        if (filterStudentRequest.getFilterEndDate().isEmpty()) {
            filterStudentRequest.setFilterEndDate(dateStr);
        }

        pageable = configSort(pageable, currentPage, totalItem, filterStudentRequest);
        System.out.println(filterStudentRequest);

        Page<StudentEntity> page;
        if (filterStudentRequest.getSearchTerm().isEmpty()) {
            page = studentRepository.findStudentsByFilter(
                    "%" + filterStudentRequest.getFilterFirstName() + "%",
                    "%" + filterStudentRequest.getFilterLastName() + "%",
                    "%" + filterStudentRequest.getFilterEmail() + "%",
                    "%" + filterStudentRequest.getFilterGender() + "%",
                    filterStudentRequest.getFilterStartDate(),
                    filterStudentRequest.getFilterEndDate(),
                    pageable);
        } else {
            page = studentRepository.findStudentsByFilterHaveKeyword(
                    filterStudentRequest.getSearchTerm(),
                    "%" + filterStudentRequest.getFilterFirstName() + "%",
                    "%" + filterStudentRequest.getFilterLastName() + "%",
                    "%" + filterStudentRequest.getFilterEmail() + "%",
                    "%" + filterStudentRequest.getFilterGender() + "%",
                    filterStudentRequest.getFilterStartDate(),
                    filterStudentRequest.getFilterEndDate(),
                    pageable);
        }

        System.out.println(page.getContent().size());
        List<StudentDTO> studentDTOS = page.getContent()
                .stream()
                .map(StudentMapper::entityToDTO).toList();

        PagingStudentResponse pagingStudentResponse = new PagingStudentResponse();
        pagingStudentResponse.setStudentDTOList(studentDTOS);
        pagingStudentResponse.setCurrentPage(filterStudentRequest.getCurrentPage());
        pagingStudentResponse.setTotalPages(page.getTotalPages());
        return pagingStudentResponse;
    }


    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public boolean createStudent(StudentDTO studentDTO) {
        StudentEntity studentSaved;
        //Check student is duplicated and email, phone duplicated or not
        if (studentRepository.findById(studentDTO.getId()).isPresent() ||
                studentRepository.findByEmail(studentDTO.getEmail()) != null ||
                studentRepository.findByPhoneNumber(studentDTO.getPhoneNumber()) != null) {
            throw new APIRequestException("Student cannot duplicate");
        }

        try {
            StudentEntity studentEntity = dtoToEntity(studentDTO);
            studentEntity.setClassEntity(classRepository.findById(studentDTO.getIdClass()).orElse(null));
            studentSaved = studentRepository.save(studentEntity);
        } catch (Exception e) {
            throw new APIRequestException(e.getMessage());
        }

        List<SubjectEntity> subjectEntities = new ArrayList<>();
        if (studentDTO.getSubjectDTOS() != null) {
            HashSet<String> subjectName = new HashSet<>();
            int i = 0;
            for (var item : studentDTO.getSubjectDTOS()) {
                subjectName.add(item.getName());
                i++;
                if (subjectName.size() < i) {
                    throw new APIRequestException("Subject cannot be duplicated");
                } else {
                    SubjectEntity subjectEntity = dtoToEntity(item);
                    subjectEntity.setStudentEntity(studentRepository.findById(studentSaved.getId()).get());
                    subjectEntities.add(subjectEntity);
                }
            }
            studentSaved.setSubjectEntities(new HashSet<>(subjectEntities));
            try {
                studentRepository.save(studentSaved);
            } catch (Exception e) {
                throw new APIRequestException("Error saving student");
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public boolean updateStudent(StudentDTO studentDTO) {
        StudentEntity studentSaved;
        //Check student is duplicated and email, phone duplicated or not
        StudentEntity oldDetailStudent = checkStudentIfExists(studentDTO.getId());
        if (studentRepository.existsByEmailAndIdNot(studentDTO.getEmail(), studentDTO.getId()) ||
                studentRepository.existsByPhoneNumberAndIdNot(studentDTO.getPhoneNumber(), studentDTO.getId())) {
            throw new APIRequestException("Email or phone is already used by another student");
        }

        try {
            StudentEntity studentEntity = dtoToEntity(studentDTO);
            studentEntity.setClassEntity(oldDetailStudent.getClassEntity());
            studentSaved = studentRepository.save(studentEntity);
        } catch (Exception e) {
            throw new APIRequestException(e.getMessage());
        }
        List<SubjectEntity> subjectEntities = new ArrayList<>();
        if (studentDTO.getSubjectDTOS() != null) {
            HashSet<String> subjectName = new HashSet<>();
            int i = 0;
            subjectRepository.deleteAllByStudentEntityId(studentSaved.getId());
            for (var item : studentDTO.getSubjectDTOS()) {
                subjectName.add(item.getName());
                i++;
                if (subjectName.size() < i) {
                    throw new APIRequestException("Subject cannot be duplicated");
                } else {
                    SubjectEntity subjectEntity = dtoToEntity(item);
                    subjectEntity.setStudentEntity(studentSaved);
                    subjectEntities.add(subjectEntity);
                }
            }
            studentSaved.setSubjectEntities(new HashSet<>(subjectEntities));
            try {
                studentRepository.save(studentSaved);
            } catch (Exception e) {
                throw new APIRequestException("Error saving student");
            }
        }
        return true;
    }

    @Override
    public boolean deleteStudent(String idStudent) {
        studentRepository.delete(checkStudentIfExists(idStudent));
        return true;
    }

    private StudentEntity checkStudentIfExists(String idStudent) {
        Optional<StudentEntity> studentEntity = studentRepository.findById(idStudent);
        if (studentEntity.isPresent()) {
            return studentEntity.get();
        } else {
            throw new APIRequestException("Could not find the student");
        }
    }

    public List<StudentDTO> getAllStudentByClassId(String classId) {
        List<StudentDTO> studentDTOList = new ArrayList<>();
        studentRepository.findAllByClassEntityId(classId).forEach(studentEntity -> {
            StudentDTO studentDTO = entityToDTO(studentEntity);
            double averageScore = 0;
            List<SubjectDTO> subjectDTOList = new ArrayList<>();
            for (var subjectEntity : studentEntity.getSubjectEntities()) {
                SubjectDTO subjectDTO = entityToDTO(subjectEntity);
                averageScore += subjectDTO.getScore();
                subjectDTOList.add(subjectDTO);
            }
            studentDTO.setAverageScore(averageScore / subjectDTOList.size());
            studentDTO.setSubjectDTOS(subjectDTOList);
            studentDTOList.add(studentDTO);
        });
        return studentDTOList;
    }

    public Pageable configSort(Pageable pageable, int currentPage, int totalPages, FilterStudentRequest filter) {
        if (filter.getSortLastName().equals("ascLN")) {
            pageable = PageRequest.of(currentPage, totalPages, Sort.by("last_name").ascending());
        } else if (filter.getSortLastName().equals("descLN")) {
            pageable = PageRequest.of(currentPage, totalPages, Sort.by("last_name").descending());
        }

        if (filter.getSortFirstName().equals("ascFN")) {
            pageable = PageRequest.of(currentPage, totalPages, Sort.by("first_name").ascending());
        } else if (filter.getSortFirstName().equals("descFN")) {
            pageable = PageRequest.of(currentPage, totalPages, Sort.by("first_name").descending());
        }

        if (filter.getSortEmail().equals("ascEmail")) {
            pageable = PageRequest.of(currentPage, totalPages, Sort.by("email").ascending());
        } else if (filter.getSortEmail().equals("descEmail")) {
            pageable = PageRequest.of(currentPage, totalPages, Sort.by("email").descending());
        }

        if (filter.getSortPhone().equals("ascPhone")) {
            pageable = PageRequest.of(currentPage, totalPages, Sort.by("phone").ascending());
        } else if (filter.getSortPhone().equals("descPhone")) {
            pageable = PageRequest.of(currentPage, totalPages, Sort.by("phone").descending());
        }

        if (filter.getSortDob().equals("ascDob")) {
            pageable = PageRequest.of(currentPage, totalPages, Sort.by("dob").ascending());
        } else if (filter.getSortDob().equals("descDob")) {
            pageable = PageRequest.of(currentPage, totalPages, Sort.by("dob").descending());
        }
        return pageable;
    }
}
