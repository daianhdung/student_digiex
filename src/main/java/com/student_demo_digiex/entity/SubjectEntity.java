package com.student_demo_digiex.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;

@Entity(name = "subject")
@Data
@EqualsAndHashCode(exclude = "studentEntity")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EntityListeners(AuditingEntityListener.class)
public class SubjectEntity {

    @Id
    @Column(length = 32)
    private String id;

    @Column(name = "name", length = 45)
    private String name;

    @Column(name = "score")
    private double score;

    @Column(name = "number_of_lessons")
    private int numberOfLessons;

    @Column(name = "status", length = 45)
    private String status;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentEntity studentEntity;


}
