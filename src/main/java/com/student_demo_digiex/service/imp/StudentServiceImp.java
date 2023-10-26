package com.student_demo_digiex.service.imp;

import com.student_demo_digiex.common.enums.Status;
import com.student_demo_digiex.common.exception.custom.APIRequestException;
import com.student_demo_digiex.common.utils.Constant;
import com.student_demo_digiex.common.utils.UniqueID;
import com.student_demo_digiex.dto.StudentDTO;
import com.student_demo_digiex.dto.SubjectDTO;
import com.student_demo_digiex.dto.mapper.StudentMapper;
import com.student_demo_digiex.dto.mapper.SubjectMapper;
import com.student_demo_digiex.entity.ClassEntity;
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

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.student_demo_digiex.dto.mapper.StudentMapper.dtoToEntityUpdate;
import static com.student_demo_digiex.dto.mapper.StudentMapper.entityToDTO;
import static com.student_demo_digiex.dto.mapper.SubjectMapper.entityToDTO;
import static com.student_demo_digiex.dto.mapper.SubjectMapper.dtoToEntity;
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
        List<StudentDTO> studentDTOList = getAllStudentWithAverageScore(studentRepository.findAllByClassId(classId));
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

        List<SubjectEntity> listSubjectEntity = subjectRepository.findAllByStudentIdAndStatus(studentEntity.getId(), Status.ACTIVE);
        for (var item : listSubjectEntity) {
            averageScore += item.getScore();
        }
        averageScore = averageScore / listSubjectEntity.size();
        studentDTO.setAverageScore(averageScore);
        return studentDTO;
    }
    @Override
    public PagingStudentResponse pagingStudentSpecification(FilterStudentRequest filterStudentRequest) {
        int totalItem = filterStudentRequest.getTotalItemEachPage();
        int currentPage = filterStudentRequest.getCurrentPage() - 1;
        if (currentPage < 0) {
            throw new APIRequestException("Page cannot less than 0");
        }
        Pageable pageable = PageRequest.of(currentPage, totalItem);

        Specification<StudentEntity> specification = ((root, cq, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(cb.equal(root.get("classId"), filterStudentRequest.getClassId()));
            String searchKey = filterStudentRequest.getSearchTerm();

            if (!searchKey.trim().isEmpty()) {
                predicateList.add(cb.or(cb.equal(root.get("firstName"), searchKey.trim())
                        , cb.equal(root.get("lastName"), searchKey.trim())
                        , cb.equal(root.get("email"), searchKey.trim())
                        , cb.equal(root.get("phoneNumber"), searchKey.trim())));
            }

            if(filterStudentRequest.getFilterGender() != null){
                predicateList.add(cb.like(root.get("gender"), filterStudentRequest.getFilterGender()));
            }

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy"); // Format to Date to query db
            try{
                if(filterStudentRequest.getFilterStartDate() != null){
                    predicateList.add(cb.greaterThanOrEqualTo(root.get("dob"), formatter.parse(filterStudentRequest.getFilterStartDate())));
                }
                if(filterStudentRequest.getFilterEndDate() != null){
                    predicateList.add(cb.lessThanOrEqualTo(root.get("dob"), formatter.parse(filterStudentRequest.getFilterEndDate())));
                }
            }catch (Exception e){
                e.printStackTrace();
                throw new APIRequestException("Invalid date filter");
            }

            Path<String> orderClause = switch (filterStudentRequest.getSortField()) {
                case "last_name" -> root.get("lastName");
                case "first_name" -> root.get("firstName");
                case "email" -> root.get("email");
                case "dob" -> root.get("dob");
                default -> throw new APIRequestException("sort field not supported");
            };
            if (!filterStudentRequest.getSortType().equals("asc") && !filterStudentRequest.getSortType().equals("desc")) {
                throw new APIRequestException("sort type not supported");
            }
            if (filterStudentRequest.getSortType().equals("asc")) {
                cq.orderBy(cb.asc(orderClause));
            } else if (filterStudentRequest.getSortType().equals("desc")) {
                cq.orderBy(cb.desc(orderClause));
            }
            return cb.and(predicateList.toArray(new Predicate[]{}));
        });


        Page<StudentEntity> pageStudentResult = studentRepository.findAll(specification, pageable);
        System.out.println(pageStudentResult.getContent());
        PagingStudentResponse pagingStudentResponse = new PagingStudentResponse()   ;
        pagingStudentResponse.setCurrentPage(filterStudentRequest.getCurrentPage());
        pagingStudentResponse.setTotalPages(pageStudentResult.getTotalPages());
        pagingStudentResponse.setStudentDTOList(
                pageStudentResult.getContent()
                        .stream()
                        .map(StudentMapper::entityToDTO).toList());
        return pagingStudentResponse;
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
            studentEntity.setClassId(createStudentRequest.getIdClass());
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
                    subjectEntity.setStudentId(studentSaved.getId());
                    subjectEntities.add(subjectEntity);
                }
            }
            try {
                subjectRepository.saveAll(subjectEntities);
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
        if (studentDTO.getEmail() != null && studentRepository.existsByEmailAndIdNot(studentDTO.getEmail(), studentDTO.getId())
                ||
                studentDTO.getPhoneNumber() != null && studentRepository.existsByPhoneNumberAndIdNot(studentDTO.getPhoneNumber(), studentDTO.getId())) {
            throw new APIRequestException("Email or phone is already used by another student");
        }
        try {
            dtoToEntityUpdate(oldDetailStudent, studentDTO);
            studentSaved = studentRepository.save(oldDetailStudent);

            if (studentDTO.getSubjectDTOS() != null) {
                List<SubjectDTO> createList = studentDTO.getSubjectDTOS()
                        .stream()
                        .filter(subjectDTO -> subjectDTO.getId() == null).toList();
                List<SubjectDTO> updateList = studentDTO.getSubjectDTOS()
                        .stream()
                        .filter(subjectDTO -> subjectDTO.getId() != null
                                && subjectDTO.getStatus() == null
                                || subjectDTO.getStatus() != null && subjectDTO.getStatus().equals(Status.ACTIVE)).toList();
                List<SubjectDTO> deleteList = studentDTO.getSubjectDTOS()
                        .stream()
                        .filter(subjectDTO -> subjectDTO.getId() != null
                                && subjectDTO.getStatus() != null
                                && subjectDTO.getStatus().equals(Status.INACTIVE)).toList();

                List<SubjectEntity> subjectEntities = subjectRepository.findAllByStudentIdAndStatus(oldDetailStudent.getId(), Status.ACTIVE);
                int sizeListAfterUpdate = subjectEntities.size() + createList.size() - deleteList.size();
                if (sizeListAfterUpdate > 5 || sizeListAfterUpdate < 3) {
                    throw new APIRequestException("Size subject cannot be less than 3 or greater than 5");
                }
                HashMap<String, String> subjectsWithIdAndName = new HashMap<>();
                subjectEntities.forEach(item -> subjectsWithIdAndName.put(item.getId(), item.getName()));


                subjectRepository.deleteAll(deleteList.stream().map(item -> {
                    subjectsWithIdAndName.remove(item.getId());
                    return dtoToEntity(item);
                }).collect(Collectors.toSet()));

                subjectRepository.saveAll(createList.stream().map(item -> {
                    if (subjectsWithIdAndName.containsValue(item.getName())) {
                        throw new APIRequestException(item.getName() + " is already exists");
                    }
                    SubjectEntity subjectEntity = dtoToEntity(item);
                    subjectEntity.setId(UniqueID.getUUID());
                    subjectEntity.setStatus(Status.ACTIVE);
                    subjectsWithIdAndName.put(item.getId(), item.getName());
                    subjectEntity.setStudentId(studentSaved.getId());
                    return subjectEntity;
                }).collect(Collectors.toSet()));

                subjectRepository.saveAll(updateList.stream().map(item -> {
                    SubjectEntity subjectEntity = subjectRepository.findSubjectById(item.getId());
                    if (item.getName() != null) {
                        subjectsWithIdAndName.remove(item.getId());
                        subjectEntity.setName(item.getName());
                    }
                    if (subjectsWithIdAndName.containsValue(item.getName().trim())) {
                        throw new APIRequestException(item.getName() + " is already exists");
                    }
                    if (item.getScore() != null) {
                        subjectEntity.setScore(item.getScore());
                    }
                    if (item.getNumberOfLessons() != null) {
                        subjectEntity.setNumberOfLessons(item.getNumberOfLessons());
                    }
                    subjectsWithIdAndName.put(item.getId(), item.getName());
                    return subjectEntity;
                }).collect(Collectors.toSet()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIRequestException(e.getMessage());
        }

        return entityToDTO(studentSaved);
    }


    @Override
    public void deleteStudent(String idStudent) {
        studentRepository.delete(checkStudentIfExists(idStudent));
        subjectRepository.deleteAllByStudentIdIsIn(Collections.singleton(idStudent));
    }

    private StudentEntity checkStudentIfExists(String idStudent) {
        Optional<StudentEntity> studentEntity = studentRepository.findById(idStudent);
        if (studentEntity.isPresent()) {
            return studentEntity.get();
        } else {
            throw new APIRequestException("Could not find the student");
        }
    }

    private List<StudentDTO> getAllStudentWithAverageScore(List<StudentEntity> studentEntityList) {
        List<StudentDTO> studentDTOList = new ArrayList<>();
        List<String> studentIdList = studentEntityList.stream().map(StudentEntity::getId).toList();
        List<SubjectEntity> subjectEntityList = subjectRepository.findAllByStudentIdIn(studentIdList);

        studentEntityList.forEach(studentEntity -> {
            StudentDTO studentDTO = entityToDTO(studentEntity);
            List<SubjectDTO> subjectDTOList = subjectEntityList
                    .stream()
                    .filter(item -> item.getStudentId().equals(studentEntity.getId()))
                    .map(SubjectMapper::entityToDTO)
                    .toList();
            double averageScore = subjectDTOList.stream().mapToDouble(SubjectDTO::getScore).sum();
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
