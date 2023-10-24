package com.student_demo_digiex.model.request;

import com.student_demo_digiex.common.enums.Status;
import com.student_demo_digiex.entity.SubjectEntity;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
public class UpdateSubjectRequest {
    private String id;

    private String name;

    @Max(value = 10, message = "Cannot higher than 10")
    @Min(value = 0, message = "Cannot lower than 0")
    private double score;

    private int numberOfLessons;

    private Status status;

    public static SubjectEntity requestToEntity(CreateSubjectRequest createSubjectRequest){
        SubjectEntity subjectEntity = new SubjectEntity();
        subjectEntity.setName(createSubjectRequest.getName());
        subjectEntity.setStatus(Status.ACTIVE);
        subjectEntity.setScore(createSubjectRequest.getScore());
        subjectEntity.setNumberOfLessons(createSubjectRequest.getNumberOfLessons());
        return subjectEntity;
    }
}
