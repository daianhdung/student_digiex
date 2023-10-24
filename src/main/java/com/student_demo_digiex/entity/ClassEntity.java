package com.student_demo_digiex.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity(name = "classes")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE classes SET status = 'INACTIVE' WHERE id = ?")
public class ClassEntity extends BaseEntity{

    @Id
    @Column(length = 32)
    private String id;

    @Column(name = "name", length = 45, unique = true)
    private String name;

    @Column(name = "max_student")
    private int maxStudent;

}
