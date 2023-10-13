package com.student_demo_digiex.controller;


import com.student_demo_digiex.dto.StudentDTO;
import com.student_demo_digiex.model.request.FilterStudentRequest;
import com.student_demo_digiex.model.response.DataResponse;
import com.student_demo_digiex.model.response.PagingStudentResponse;
import com.student_demo_digiex.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentService studentService;

    @GetMapping("rank/{status}")
    public ResponseEntity<?> getStudentByRank(@PathVariable("status") String status){
        DataResponse dataResponse = new DataResponse();
        dataResponse.setData(studentService.getStudentById(id));
        dataResponse.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") String id){
        DataResponse dataResponse = new DataResponse();
        dataResponse.setData(studentService.getStudentById(id));
        dataResponse.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<?> getAllStudent(@PathVariable("classId") String classId){
        DataResponse dataResponse = new DataResponse();
        dataResponse.setData(studentService.getAllStudentByClassIdDefaultSortHighScore(classId));
        dataResponse.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody @Valid StudentDTO studentDTO){
        studentService.createStudent(studentDTO);
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(200);
        dataResponse.setDesc("Create Student successfully");
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @PostMapping("/filter")
    public ResponseEntity<?> getStudentByFilter(@RequestBody FilterStudentRequest filterStudentRequest) {
        PagingStudentResponse pagingStudentResponse = studentService.pagingStudent(filterStudentRequest);
        DataResponse dataResponse = new DataResponse();
        dataResponse.setDesc("Get paging student successfully");
        dataResponse.setData(pagingStudentResponse);
        dataResponse.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable("id") String id, @RequestBody @Valid StudentDTO studentDTO){
        studentDTO.setId(id);
        studentService.updateStudent(studentDTO);
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(200);
        dataResponse.setDesc("Update Student successfully");
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudentById(@PathVariable("id") String id){
        DataResponse dataResponse = new DataResponse();
        dataResponse.setData(studentService.deleteStudent(id));
        dataResponse.setDesc("Delete student successfully");
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }
}
