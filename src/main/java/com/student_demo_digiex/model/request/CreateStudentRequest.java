package com.student_demo_digiex.model.request;


import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public class CreateStudentRequest {

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email
    private String email;

    private Date dob;

    private String address;

    private String gender;

    @Pattern(regexp = "\\d{10}$", message = "Phone number must be 10 digits")
    @NotNull(message = "Phone number is mandatory")
    private String phoneNumber;

    @Size(min = 3, max = 5, message = "must have min 3 subject and max 5")
    @Valid
    private List<CreateSubjectRequest> subjectRequestList;

    @NotBlank(message = "Class Id is mandatory")
    private String idClass;
}
