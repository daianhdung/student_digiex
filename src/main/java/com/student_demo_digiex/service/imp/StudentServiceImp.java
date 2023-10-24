package com.student_demo_digiex.service.imp;

import com.student_demo_digiex.common.enums.Status;
import com.student_demo_digiex.common.exception.custom.APIRequestException;
import com.student_demo_digiex.common.utils.Constant;
import com.student_demo_digiex.common.utils.UniqueID;
import com.student_demo_digiex.dto.StudentDTO;
import com.student_demo_digiex.dto.SubjectDTO;
import com.student_demo_digiex.dto.mapper.StudentMapper;
import com.student_demo_digiex.entity.StudentEntity;
import com.student_demo_digiex.entity.SubjectEntity;
import com.student_demo_digiex.model.request.CreateStudentRequest;
import com.student_demo_digiex.model.request.FilterStudentRequest;
import com.student_demo_digiex.model.response.PagingStudentResponse;
import com.student_demo_digiex.repository.ClassRepository;
import com.student_demo_digiex.repository.StudentRepository;
import com.student_demo_digiex.repository.SubjectRepository;
import com.student_demo_digiex.service.ClassService;
import com.student_demo_digiex.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.student_demo_digiex.dto.mapper.StudentMapper.dtoToEntityUpdate;
import static com.student_demo_digiex.dto.mapper.StudentMapper.entityToDTO;
import static com.student_demo_digiex.dto.mapper.SubjectMapper.dtoToEntity;
import static com.student_demo_digiex.dto.mapper.SubjectMapper.entityToDTO;
import static com.student_demo_digiex.model.request.CreateSubjectRequest.requestToEntity;
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
    public List<StudentDTO> get3StudentSortByScoreAndDob(String rank) {
        return switch (rank) {
            case "EXCELLENT" -> findTop3ByScoreBetweenOrderByScoreDescDobAsc(Constant.MAX, Constant.EXCELLENT);
            case "GOOD" -> findTop3ByScoreBetweenOrderByScoreDescDobAsc(Constant.EXCELLENT, Constant.GOOD);
            case "AVERAGE" -> findTop3ByScoreBetweenOrderByScoreDescDobAsc(Constant.GOOD, Constant.AVERAGE);
            case "WEAK" -> findTop3ByScoreBetweenOrderByScoreDescDobAsc(Constant.AVERAGE, Constant.WEAK);
            case "POOR" -> findTop3ByScoreBetweenOrderByScoreDescDobAsc(Constant.WEAK, Constant.POOR);
            default -> throw new APIRequestException("Rank invalid");
        };
    }

    @Override
    public List<StudentDTO> getAllStudentByClassIdDefaultSortHighScore(String classId) {
        classService.checkClassIfExists(classId);

        List<StudentDTO> studentDTOList = getAllStudentWithAverageScore(studentRepository.findAllByClassEntityId(classId));
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

        if (!filterStudentRequest.getSortField().equals("dob") && !filterStudentRequest.getSortField().equals("email") &&
                !filterStudentRequest.getSortField().equals("lastName") && !filterStudentRequest.getSortField().equals("firstName")
                && !filterStudentRequest.getSortField().equals("phoneNumber")) {
            throw new APIRequestException("Not support this sort field");
        }
        if (!filterStudentRequest.getSortType().equals("asc") && !filterStudentRequest.getSortType().equals("desc")) {
            throw new APIRequestException("Not support this sort type");
        }

        Pageable pageable = PageRequest.of(currentPage, totalItem);
        if (filterStudentRequest.getSortType().equals("desc")) {
            pageable = PageRequest.of(currentPage, totalItem, Sort.by(filterStudentRequest.getSortField()).descending());
        } else if (filterStudentRequest.getSortType().equals("asc")) {
            pageable = PageRequest.of(currentPage, totalItem, Sort.by(filterStudentRequest.getSortField()).ascending());

        }

        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Định dạng của chuỗi đầu ra

        if (filterStudentRequest.getFilterStartDate() == null) {
            filterStudentRequest.setFilterStartDate("1900-01-01");
        } else {
            filterStudentRequest.setFilterStartDate(sdf.format(filterStudentRequest.getFilterStartDate()));
        }

        if (filterStudentRequest.getFilterEndDate() == null) {
            //Default endDate now()
            String dateStr = sdf.format(currentDate);
            filterStudentRequest.setFilterEndDate(dateStr);
        } else {
            filterStudentRequest.setFilterEndDate(sdf.format(filterStudentRequest.getFilterEndDate()));
        }

        Page<StudentEntity> page;
//        if (filterStudentRequest.getSearchTerm().trim().isEmpty()) {
//            page = studentRepository.findStudentsByFilter(
//                    "%" + filterStudentRequest.getFilterFirstName() + "%",
//                    "%" + filterStudentRequest.getFilterLastName() + "%",
//                    "%" + filterStudentRequest.getFilterEmail() + "%",
//                    "%" + filterStudentRequest.getFilterGender() + "%",
//                    filterStudentRequest.getFilterStartDate(),
//                    filterStudentRequest.getFilterEndDate(),
//                    pageable);
//        } else {
        if (filterStudentRequest.getFilterGender() == null) {
            filterStudentRequest.setFilterGender("ale");
        }
        page = studentRepository.findStudentsByFilterHaveKeyword(
                "%" + filterStudentRequest.getSearchTerm() + "%",
                "%" + filterStudentRequest.getFilterGender() + "%",
                filterStudentRequest.getFilterStartDate(),
                filterStudentRequest.getFilterEndDate(),
                pageable);
//        }

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
    public PagingStudentResponse pagingStudentSpecification(FilterStudentRequest filterStudentRequest) {
        Specification<StudentEntity> specification = ((root, cq, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();


            return cb.and(predicateList.toArray(new Predicate[]{}));
        });


        return null;
    }


    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public StudentDTO createStudent(CreateStudentRequest createStudentRequest) {
        if (classRepository.findByIdAndStatus(createStudentRequest.getIdClass(), Status.ACTIVE) == null) {
            throw new APIRequestException("Cannot find class");
        }

        StudentEntity studentSaved;
        //Check student is duplicated and email, phone duplicated or not
        if (studentRepository.findByEmail(createStudentRequest.getEmail()) != null ||
                studentRepository.findByPhoneNumber(createStudentRequest.getPhoneNumber()) != null) {
            throw new APIRequestException("This email or phone number is already used");
        }
        StudentEntity studentEntity = new StudentEntity();
        try {

            studentEntity.setId(UniqueID.getUUID());
            studentEntity.setEmail(createStudentRequest.getEmail().trim());
            studentEntity.setStatus(Status.ACTIVE);
            studentEntity.setDob(createStudentRequest.getDob());
            studentEntity.setGender(createStudentRequest.getGender());
            studentEntity.setPhoneNumber(createStudentRequest.getPhoneNumber().trim());
            studentEntity.setLastName(createStudentRequest.getLastName().trim());
            studentEntity.setAddress(createStudentRequest.getAddress().trim());
            studentEntity.setFirstName(createStudentRequest.getFirstName().trim());
            studentEntity.setClassEntity(classRepository.findById(createStudentRequest.getIdClass()).orElse(null));
            studentSaved = studentRepository.save(studentEntity);
        } catch (Exception e) {
            throw new APIRequestException(e.getMessage());
        }

        List<SubjectEntity> subjectEntities = new ArrayList<>();
        if (createStudentRequest.getSubjectRequestList() != null) {
            HashSet<String> subjectName = new HashSet<>();
            int i = 0;
            for (var item : createStudentRequest.getSubjectRequestList()) {
                subjectName.add(item.getName().trim());
                i++;
                if (subjectName.size() < i) {
                    throw new APIRequestException("Subject cannot be duplicated");
                } else {
                    SubjectEntity subjectEntity = requestToEntity(item);
                    subjectEntity.setId(UniqueID.getUUID());
                    subjectEntity.setStatus(Status.ACTIVE);
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
        return entityToDTO(studentSaved);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public StudentDTO updateStudent(StudentDTO studentDTO) {
        StudentEntity studentSaved;
        //Check student is duplicated and email, phone duplicated or not
        StudentEntity oldDetailStudent = checkStudentIfExists(studentDTO.getId());
        if (studentRepository.existsByEmailAndIdNot(studentDTO.getEmail(), studentDTO.getId()) ||
                studentRepository.existsByPhoneNumberAndIdNot(studentDTO.getPhoneNumber(), studentDTO.getId())) {
            throw new APIRequestException("Email or phone is already used by another student");
        }
        try {
            dtoToEntityUpdate(oldDetailStudent, studentDTO);
            studentSaved = studentRepository.save(oldDetailStudent);
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
                    if (subjectEntity.getId() == null) {
                        subjectEntity.setId(UniqueID.getUUID());
                    }
                    subjectEntity.setStudentEntity(studentSaved);
                    subjectEntities.add(subjectEntity);
                }
            }
            studentSaved.setSubjectEntities(new HashSet<>(subjectEntities));
            try {
                studentRepository.save(studentSaved);
            } catch (Exception e) {
                e.printStackTrace();
                throw new APIRequestException("Error saving student");
            }
        }
        return entityToDTO(studentSaved);
    }

    @Override
    public void deleteStudent(String idStudent) {
        studentRepository.delete(checkStudentIfExists(idStudent));
    }

    private StudentEntity checkStudentIfExists(String idStudent) {
        Optional<StudentEntity> studentEntity = studentRepository.findById(idStudent);
        if (studentEntity.isPresent()) {
            return studentEntity.get();
        } else {
            throw new APIRequestException("Could not find the student");
        }
    }

    public List<StudentDTO> getAllStudentWithAverageScore(List<StudentEntity> studentEntityList) {
        List<StudentDTO> studentDTOList = new ArrayList<>();
        studentEntityList.forEach(studentEntity -> {
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

    public List<StudentDTO> findTop3ByScoreBetweenOrderByScoreDescDobAsc(double maxScore, double minScore) {
        List<StudentDTO> studentDTOList = getAllStudentWithAverageScore
                (studentRepository.findAll());

        //Filter get only 3 student with appropriate score
        if (maxScore == Constant.MAX) {
            studentDTOList = studentDTOList.stream()
                    .filter(studen -> studen.getAverageScore() >= Constant.EXCELLENT)
                    .sorted((o1, o2) -> compare(o2.getAverageScore(), o1.getAverageScore()))
                    .sorted(Comparator.comparingDouble(StudentDTO::getAverageScore))
                    .limit(3)
                    .collect(Collectors.toList());
        } else if (minScore == Constant.POOR) {
            studentDTOList = studentDTOList.stream()
                    .filter(studen -> studen.getAverageScore() < Constant.WEAK)
                    .sorted(Comparator.comparingDouble(StudentDTO::getAverageScore))
                    .limit(3)
                    .collect(Collectors.toList());
        } else {
            studentDTOList = studentDTOList.stream()
                    .filter(student ->
                            student.getAverageScore() < maxScore
                                    && student.getAverageScore() >= minScore)
                    .sorted(Comparator.comparingDouble(StudentDTO::getAverageScore))
                    .limit(3)
                    .collect(Collectors.toList());
        }

        //If those average scores are equal, then sort by dob asc
        studentDTOList.sort((s1, s2) -> {
            int compareValue = Double.compare(s2.getAverageScore(), s1.getAverageScore());
            if (compareValue == 0) {
                compareValue = s1.getDob().compareTo(s2.getDob());
            }
            return compareValue;
        });
        return studentDTOList;
    }

    ;
}
