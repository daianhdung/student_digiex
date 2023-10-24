package com.student_demo_digiex.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UpdateStudentRequest {

    private String firstName;

    private String lastName;

    @Email
    private String email;

    private Date dob;

    private String address;

    private String gender;

    @Pattern(regexp = "\\d{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @Size(min = 3, max = 5, message = "must have min 3 subject and max 5")
    @Valid
    private List<UpdateSubjectRequest> subjectRequestList;

    private String idClass;
}
