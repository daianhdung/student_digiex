package com.student_demo_digiex.controller;


import com.student_demo_digiex.common.utils.RestAPIStatus;
import com.student_demo_digiex.dto.StudentDTO;
import com.student_demo_digiex.model.request.FilterStudentRequest;
import com.student_demo_digiex.model.response.DataResponse;
import com.student_demo_digiex.model.response.PagingStudentResponse;
import com.student_demo_digiex.repository.StudentRepository;
import com.student_demo_digiex.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/student")
public class StudentController extends BaseController{

    @Autowired
    StudentService studentService;
    @Autowired
    StudentRepository studentRepository;

    @GetMapping("rank/{rank}")
    public ResponseEntity<?> getStudentByRank(@PathVariable("rank") String rank){
        return responseUtil.buildResponse(RestAPIStatus.OK, studentService.get3StudentSortByScoreAndDob(rank.toUpperCase())
                , HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") String id){
        return responseUtil.buildResponse(RestAPIStatus.OK, studentService.getStudentById(id)
                , HttpStatus.OK);
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<?> getAllStudent(@PathVariable("classId") String classId){
        return responseUtil.buildResponse(RestAPIStatus.OK, studentService.getAllStudentByClassIdDefaultSortHighScore(classId)
                , HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody @Valid StudentDTO studentDTO){
        boolean isSuccess = studentService.createStudent(studentDTO);
        return responseUtil.buildResponse(RestAPIStatus.OK, isSuccess
                , "Create Student successfully", HttpStatus.OK);
    }

    @PostMapping("/filter")
    public ResponseEntity<?> getStudentByFilter(@RequestBody FilterStudentRequest filterStudentRequest) {
        return responseUtil.buildResponse(RestAPIStatus.OK, studentService.pagingStudent(filterStudentRequest)
                , "Get paging student successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable("id") String id, @RequestBody @Valid StudentDTO studentDTO){
        studentDTO.setId(id);
        boolean isSuccess = studentService.updateStudent(studentDTO);
        return responseUtil.buildResponse(RestAPIStatus.OK, isSuccess
                , "Update Student successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudentById(@PathVariable("id") String id){
        boolean isSuccess = studentService.deleteStudent(id);
        return responseUtil.buildResponse(RestAPIStatus.OK, isSuccess
                , "Delete student successfully", HttpStatus.OK);
    }
}
