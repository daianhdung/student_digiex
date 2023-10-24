package com.student_demo_digiex.controller;

import com.student_demo_digiex.common.utils.RestAPIStatus;
import com.student_demo_digiex.dto.ClassDTO;
import com.student_demo_digiex.model.request.CreateClassRequest;
import com.student_demo_digiex.model.request.FilterClassRequest;
import com.student_demo_digiex.model.request.UpdateClassRequest;
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

    @PostMapping
    public ResponseEntity<?> createClass(@RequestBody @Valid CreateClassRequest createClass){
        return responseUtil.buildResponse(RestAPIStatus.OK, classService.createClass(createClass)
                , "Create Class successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClassById(@PathVariable("id") String id){
        return responseUtil.buildResponse(RestAPIStatus.OK, classService.getClassById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllClass(){
        return responseUtil.buildResponse(RestAPIStatus.OK, classService.getAllClass(), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getClassByFilter(@RequestParam("currentPage") int currentPage,
                                              @RequestParam(value = "searchKeyword") String searchKeyword,
                                              @RequestParam(value = "sortField") String sortField,
                                              @RequestParam(value = "sortType") String sortType,
                                              @RequestParam("totalItemEachPage") int totalItemEachPage) {
        FilterClassRequest filterClassRequest = new FilterClassRequest(currentPage, searchKeyword,
                sortField, sortType, totalItemEachPage);
        return responseUtil.buildResponse(RestAPIStatus.OK, classService.getClassByFilter(filterClassRequest)
                , "Get paging classes successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClass(
            @PathVariable("id") String id,
            @RequestBody @Valid UpdateClassRequest updateClass){
        ClassDTO classDTO = new ClassDTO(id, updateClass.getName(), updateClass.getMaxStudent());
        return responseUtil.buildResponse(RestAPIStatus.OK, classService.updateClass(classDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClassById(@PathVariable("id") String id){
        classService.deleteClass(id);
        return responseUtil.buildResponse(RestAPIStatus.OK, ""
                , "Delete class successfully", HttpStatus.OK);
    }
}
