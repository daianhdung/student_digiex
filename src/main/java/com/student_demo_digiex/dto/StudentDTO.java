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

    public StudentDTO() {
    }

    public StudentDTO(String idStudent, UpdateStudentRequest updateStudentRequest) {
        this.id = idStudent;
        this.firstName = updateStudentRequest.getFirstName() != null ? updateStudentRequest.getFirstName() : null;
        this.lastName = updateStudentRequest.getLastName() != null ? updateStudentRequest.getLastName() : null;
        this.email = updateStudentRequest.getEmail() != null ? updateStudentRequest.getEmail() : null;
        this.dob = updateStudentRequest.getDob() != null ? updateStudentRequest.getDob() : null;
        this.address = updateStudentRequest.getAddress() != null ? updateStudentRequest.getAddress() : null;
        this.gender = updateStudentRequest.getGender() != null ? updateStudentRequest.getGender() : null;
        this.phoneNumber = updateStudentRequest.getPhoneNumber() != null ? updateStudentRequest.getPhoneNumber() : null;
        this.idClass = updateStudentRequest.getIdClass() != null ? updateStudentRequest.getIdClass() : null;
        this.subjectDTOS = updateStudentRequest.getSubjectRequestList().stream().map(item -> {
            SubjectDTO subjectDTO = new SubjectDTO();
            subjectDTO.setNumberOfLessons(item.getNumberOfLessons() != null ? item.getNumberOfLessons() : null);
            subjectDTO.setId(item.getId() != null ? item.getId() : null);
            subjectDTO.setStatus(item.getStatus() != null ? item.getStatus() : null);
            subjectDTO.setName(item.getName() != null ? item.getName() : null);
            subjectDTO.setScore(item.getScore() != null ? item.getScore() : null);
            return subjectDTO;
        }).collect(Collectors.toList());
    }
}
