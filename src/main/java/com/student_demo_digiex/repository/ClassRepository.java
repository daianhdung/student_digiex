package com.student_demo_digiex.repository;

import com.student_demo_digiex.entity.ClassEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, String> {

    @Query(value = "SELECT count(*) FROM student WHERE class_id = :idClass", nativeQuery = true)
    int countStudentInClass(String idClass);
    ClassEntity getClassEntitiesByName(String name);

    @Query(value
            = "SELECT * FROM classes c LEFT JOIN student s ON c.id = s.class_id LEFT JOIN subject sj ON s.id = sj.student_id;", nativeQuery = true)
    List<ClassEntity> findAllJoinStudentAndSubject();

    List<ClassEntity> findAll();

    Page<ClassEntity> findByNameContainingIgnoreCase(String keyword , Pageable pageable);
}
