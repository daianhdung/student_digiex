package com.student_demo_digiex.controller;

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
public class ClassController {

    @Autowired
    ClassService classService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getClassById(@PathVariable("id") String id){
        DataResponse dataResponse = new DataResponse();
        dataResponse.setData(classService.getClassById(id));
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllClass(){
        DataResponse dataResponse = new DataResponse();
        dataResponse.setData(classService.getAllClass());
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createClass(@RequestBody @Valid ClassDTO classDTO){
        classService.createClass(classDTO);
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(200);
        dataResponse.setDesc("Create Class successfully");
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @PostMapping("/filter")
    public ResponseEntity<?> getClassByFilter(@RequestBody FilterClassRequest filterClassRequest) {
        PagingClassResponse pagingClassResponse = classService.getClassByFilter(filterClassRequest);
        DataResponse dataResponse = new DataResponse();
        dataResponse.setDesc("Get paging classes successfully");
        dataResponse.setData(pagingClassResponse);
        dataResponse.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClass(@PathVariable("id") String id, @RequestBody @Valid ClassDTO classDTO){
        classDTO.setId(id);
        classService.updateClass(classDTO);
        DataResponse dataResponse = new DataResponse();
        dataResponse.setStatus(200);
        dataResponse.setDesc("Update Class successfully");
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClassById(@PathVariable("id") String id){
        DataResponse dataResponse = new DataResponse();
        dataResponse.setData(classService.deleteClass(id));
        dataResponse.setDesc("Delete class successfully");
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }
}
