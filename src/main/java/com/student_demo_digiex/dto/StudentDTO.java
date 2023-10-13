package com.student_demo_digiex.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class StudentDTO {

    @NotNull(message = "Id is mandatory")
    private String id;

    @NotNull(message = "First name is mandatory")
    private String firstName;

    @NotNull(message = "Last name is mandatory")
    private String lastName;

    @NotNull(message = "Email is mandatory")
    @Email
    private String email;

    private Date dob;

    private String address;

    private String gender;

    @Pattern(regexp = "\\d{10}$", message = "Phone number must be 10 digits")
    @NotNull(message = "Phone number is mandatory")
    private String phoneNumber;

    private String status;

    @Size(min = 3, max = 5, message = "must have min 3 subject and max 5")
    @Valid
    private List<SubjectDTO> subjectDTOS;

    private String idClass;

    private double averageScore;
}
