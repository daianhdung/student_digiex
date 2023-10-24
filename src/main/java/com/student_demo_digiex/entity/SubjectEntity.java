package com.student_demo_digiex.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity(name = "subject")
@Data
//@EqualsAndHashCode(exclude = "studentEntity")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE subject SET status = 'INACTIVE' WHERE id = ?")
public class SubjectEntity extends BaseEntity{

    @Id
    @Column(length = 32)
    private String id;

    @Column(name = "name", length = 45)
    private String name;

    @Column(name = "score")
    private double score;

    @Column(name = "number_of_lessons")
    private int numberOfLessons;

    @Column(name = "student_id")
    private String studentId;


}
