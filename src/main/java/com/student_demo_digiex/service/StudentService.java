package com.student_demo_digiex.service;

import com.student_demo_digiex.dto.StudentDTO;
import com.student_demo_digiex.model.request.FilterStudentRequest;
import com.student_demo_digiex.model.response.PagingStudentResponse;

import java.util.List;

public interface StudentService {

    List<StudentDTO> getAllStudentByClassIdDefaultSortHighScore(String classId);
    StudentDTO getStudentById(String idStudent);

    PagingStudentResponse pagingStudent(FilterStudentRequest filterStudentRequest);

    boolean createStudent(StudentDTO studentDTO);

    boolean updateStudent(StudentDTO studentDTO);

    boolean deleteStudent(String idStudent);
}
