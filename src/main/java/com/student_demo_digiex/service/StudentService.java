package com.student_demo_digiex.service;

import com.student_demo_digiex.dto.StudentDTO;
import com.student_demo_digiex.model.request.CreateStudentRequest;
import com.student_demo_digiex.model.request.FilterStudentRequest;
import com.student_demo_digiex.model.response.PagingStudentResponse;

import java.util.List;

public interface StudentService {

    List<StudentDTO> get3StudentSortByScoreAndDob(String rank);
    List<StudentDTO> getAllStudentByClassIdDefaultSortHighScore(String classId);
    StudentDTO getStudentById(String idStudent);

    PagingStudentResponse pagingStudent(FilterStudentRequest filterStudentRequest);

    PagingStudentResponse pagingStudentSpecification(FilterStudentRequest filterStudentRequest);

    StudentDTO createStudent(CreateStudentRequest createStudentRequest);

    StudentDTO updateStudent(StudentDTO studentDTO);

    void deleteStudent(String idStudent);
}
