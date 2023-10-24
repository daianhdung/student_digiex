package com.student_demo_digiex.repository;

import com.student_demo_digiex.common.enums.Status;
import com.student_demo_digiex.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, String> {

    int deleteAllByStudentIdIsIn(Collection<String> studentId);

    SubjectEntity findSubjectById (String subjectId);

    List<SubjectEntity> findAllByStudentIdAndStatus(String studentId, Status status);
    List<SubjectEntity> findAllByStudentIdIn(List<String> studentIds);
}
