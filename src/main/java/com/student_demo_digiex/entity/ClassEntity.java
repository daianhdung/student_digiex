package com.student_demo_digiex.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;

@Data
@Entity(name = "classes")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EntityListeners(AuditingEntityListener.class)
public class ClassEntity extends BaseEntity{

    @Id
    @Column(length = 32)
    private String id;

    @Column(name = "name", length = 45, unique = true)
    private String name;

    @Column(name = "max_student")
    private int maxStudent;

    @Column(name = "status", length = 45)
    private String status;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "classEntity")
    private Set<StudentEntity> studentEntitySet;
}
