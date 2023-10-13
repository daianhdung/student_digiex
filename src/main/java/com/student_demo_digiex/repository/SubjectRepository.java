package com.student_demo_digiex.repository;

import com.student_demo_digiex.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, String> {

    int deleteAllByStudentEntityId(String studentId);
}
