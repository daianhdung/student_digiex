package com.student_demo_digiex.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.student_demo_digiex.common.enums.Status;
import com.student_demo_digiex.model.request.UpdateStudentRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentDTO {
    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private Date dob;

    private String address;

    private String gender;

    private String phoneNumber;

    private Status status;

    private List<SubjectDTO> subjectDTOS;

    private String idClass;

    private Double averageScore;

    public StudentDTO(){}

    public StudentDTO(String idStudent, UpdateStudentRequest updateStudentRequest) {
        this.id = idStudent;
        this.firstName = updateStudentRequest.getFirstName();
        this.lastName = updateStudentRequest.getLastName();
        this.email = updateStudentRequest.getEmail();
        this.dob = updateStudentRequest.getDob();
        this.address = updateStudentRequest.getAddress();
        this.gender = updateStudentRequest.getGender();
        this.phoneNumber = updateStudentRequest.getPhoneNumber();
        this.idClass = updateStudentRequest.getIdClass();
        this.subjectDTOS = updateStudentRequest.getSubjectRequestList().stream().map(item -> {
            SubjectDTO subjectDTO = new SubjectDTO();
            subjectDTO.setNumberOfLessons(item.getNumberOfLessons());
            subjectDTO.setId(item.getId());
            subjectDTO.setIdStudent(idStudent);
            subjectDTO.setStatus(item.getStatus());
            subjectDTO.setName(item.getName());
            subjectDTO.setScore(item.getScore());
            return subjectDTO;
        }).collect(Collectors.toList());
    }
}
