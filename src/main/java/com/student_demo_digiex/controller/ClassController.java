package com.student_demo_digiex.controller;

import com.student_demo_digiex.common.utils.ResponseUtil;
import com.student_demo_digiex.common.utils.RestAPIStatus;
import com.student_demo_digiex.dto.ClassDTO;
import com.student_demo_digiex.model.request.FilterClassRequest;
import com.student_demo_digiex.model.response.DataResponse;
import com.student_demo_digiex.model.response.PagingClassResponse;
import com.student_demo_digiex.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/class")
@CrossOrigin
public class ClassController extends BaseController{

    @Autowired
    ClassService classService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getClassById(@PathVariable("id") String id){
        return responseUtil.buildResponse(RestAPIStatus.OK, classService.getClassById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllClass(){
        return responseUtil.buildResponse(RestAPIStatus.OK, classService.getAllClass(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createClass(@RequestBody @Valid ClassDTO classDTO){
        boolean isSuccess = classService.createClass(classDTO);
        return responseUtil.buildResponse(RestAPIStatus.OK, isSuccess, "Create Class successfully", HttpStatus.OK);
    }

    @PostMapping("/filter")
    public ResponseEntity<?> getClassByFilter(@RequestBody FilterClassRequest filterClassRequest) {
        return responseUtil.buildResponse(RestAPIStatus.OK, classService.getClassByFilter(filterClassRequest)
                , "Get paging classes successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClass(@PathVariable("id") String id, @RequestBody @Valid ClassDTO classDTO){
        classDTO.setId(id);
        boolean isSuccess = classService.updateClass(classDTO);
        return responseUtil.buildResponse(RestAPIStatus.OK, isSuccess
                , "Update Class successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClassById(@PathVariable("id") String id){
        boolean isSuccess = classService.deleteClass(id);
        return responseUtil.buildResponse(RestAPIStatus.OK, isSuccess
                , "Delete class successfully", HttpStatus.OK);
    }
}
