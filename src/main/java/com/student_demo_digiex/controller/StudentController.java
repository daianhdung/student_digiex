package com.student_demo_digiex.controller;


import com.student_demo_digiex.common.utils.RestAPIStatus;
import com.student_demo_digiex.dto.StudentDTO;
import com.student_demo_digiex.model.request.CreateStudentRequest;
import com.student_demo_digiex.model.request.FilterStudentRequest;
import com.student_demo_digiex.model.request.UpdateStudentRequest;
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

    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody @Valid CreateStudentRequest createStudentRequest){
        return responseUtil.buildResponse(RestAPIStatus.OK, studentService.createStudent(createStudentRequest)
                , "Create Student successfully", HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getStudentByFilter(@RequestParam("classId") String classId,
                                                @RequestParam("searchTerm") String searchTerm,
                                                @RequestParam("sortField") String sortField,
                                                @RequestParam("sortType") String sortType,
                                                @RequestParam(value = "filterGender", required = false) String filterGender,
                                                @RequestParam(value = "filterStartDate", required = false) String filterStartDate,
                                                @RequestParam(value = "filterEndDate", required = false) String filterEndDate,
                                                @RequestParam("totalItemEachPage") Integer totalItemEachPage,
                                                @RequestParam("currentPage") Integer currentPage
                                                ) {
        FilterStudentRequest filterStudentRequest = new FilterStudentRequest(classId, searchTerm, sortField, sortType
                , filterGender, filterStartDate, filterEndDate, totalItemEachPage
        , currentPage);
        return responseUtil.buildResponse(RestAPIStatus.OK, studentService.pagingStudent(filterStudentRequest)
                , "Get paging student successfully", HttpStatus.OK);
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

    @GetMapping("rank/{rank}")
    public ResponseEntity<?> getStudentByRank(@PathVariable("rank") String rank){
        return responseUtil.buildResponse(RestAPIStatus.OK, studentService.get3StudentSortByScoreAndDob(rank.toUpperCase())
                , HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(
            @PathVariable("id") String id,
            @RequestBody @Valid UpdateStudentRequest updateStudentRequest)
    {
        StudentDTO studentDTO = new StudentDTO(id, updateStudentRequest);
        return responseUtil.buildResponse(RestAPIStatus.OK, studentService.updateStudent(studentDTO)
                , "Update Student successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudentById(@PathVariable("id") String id){
        studentService.deleteStudent(id);
        return responseUtil.buildResponse(RestAPIStatus.OK, ""
                , "Delete student successfully", HttpStatus.OK);
    }
}
