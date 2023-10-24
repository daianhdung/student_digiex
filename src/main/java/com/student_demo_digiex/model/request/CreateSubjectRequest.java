package com.student_demo_digiex.model.request;

import com.student_demo_digiex.common.enums.Status;
import com.student_demo_digiex.entity.SubjectEntity;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateSubjectRequest {

    private String id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @Max(value = 10, message = "Cannot higher than 10")
    @Min(value = 0, message = "Cannot lower than 0")
    private double score;

    @NotNull(message = "Number of Lessons is mandatory")
    private int numberOfLessons;

    public static SubjectEntity requestToEntity(CreateSubjectRequest createSubjectRequest){
        SubjectEntity subjectEntity = new SubjectEntity();
        subjectEntity.setName(createSubjectRequest.getName());
        subjectEntity.setScore(createSubjectRequest.getScore());
        subjectEntity.setNumberOfLessons(createSubjectRequest.getNumberOfLessons());
        return subjectEntity;
    }
}
