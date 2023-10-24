package com.student_demo_digiex.repository;

import com.student_demo_digiex.entity.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, String>, JpaSpecificationExecutor<StudentEntity> {

    StudentEntity findByEmail(String email);
    StudentEntity findByPhoneNumber(String phoneNumber);

    List<StudentEntity> findAllByClassEntityId(String classId);

    @Query(value = "SELECT sj.name FROM student s LEFT JOIN subject sj ON s.id = sj.student_id", nativeQuery = true)
    Set<String> getListSubjectNameByStudentId(String studentId);

    boolean existsByEmailAndIdNot(String email, String idStudent);
    boolean existsByPhoneNumberAndIdNot(String phone, String idStudent);

    @Query(value =
            "SELECT * FROM student s " +
                    "WHERE (s.first_name LIKE :searchTerm " +
                    "OR s.last_name LIKE :searchTerm " +
                    "OR s.email LIKE :searchTerm " +
                    "OR s.phone_number LIKE :searchTerm) " +
                    "AND (s.gender LIKE :gender " +
                    "AND s.dob >= :dobMin " +
                    "AND s.dob <= :dobMax)",
            nativeQuery = true)
    Page<StudentEntity> findStudentsByFilterHaveKeyword(
            @Param("searchTerm") String searchTerm,
            @Param("gender") String gender,
            @Param("dobMin") String dobMin,
            @Param("dobMax") String dobMax, Pageable pageable);


//    @Query(value =
//            "SELECT * FROM student s " +
//                    "WHERE  s.first_name LIKE :firstNameContains " +
//                    "AND s.last_name LIKE :lastNameContains " +
//                    "AND s.email LIKE :emailContains " +
//                    "AND s.gender LIKE :gender " +
//                    "AND s.dob >= :dobMin " +
//                    "AND s.dob <= :dobMax",
//            nativeQuery = true)
//    Page<StudentEntity> findStudentsByFilter(
//            @Param("firstNameContains") String firstNameContains,
//            @Param("lastNameContains") String lastNameContains,
//            @Param("emailContains") String emailContains,
//            @Param("gender") String gender,
//            @Param("dobMin") String dobMin,
//            @Param("dobMax") String dobMax, Pageable pageable);



}
