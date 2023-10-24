package com.student_demo_digiex.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity(name = "student")
@Data
@EqualsAndHashCode(exclude = "classEntity")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE student SET status = 'INACTIVE' WHERE id = ?")
public class StudentEntity extends BaseEntity{

    @Id
    @Column(length = 32)
    private String id;

    @Column(name = "first_name", length = 45)
    private String firstName;

    @Column(name = "last_name", length = 45)
    private String lastName;

    @Column(name = "email", length = 45, unique = true)
    private String email;

    @Column(name = "dob")
    private Date dob;

    @Column(name = "address")
    private String address;

    @Column(name = "gender", length = 45)
    private String gender;

    @Column(name = "phone_number", length = 10, unique = true)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studentEntity")
    private Set<SubjectEntity> subjectEntities;
}
