package com.student_demo_digiex.model.request;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

@Getter
@Setter
public class PagingRequest {

    private String sortField;
    private Sort.Direction sortType = Sort.Direction.ASC;
    private Integer pageNo;
    private Integer pageSize;

    public Pageable getPageable(FilterStudentRequest filterStudentRequest){
        this.sortField = Objects.nonNull(filterStudentRequest.getSortField()) ? filterStudentRequest.getSortField() : this.sortField;
        this.sortType = Objects.nonNull(filterStudentRequest.getSortField()) ? Sort.Direction.valueOf(filterStudentRequest.getSortType()) : this.sortType;
        this.pageNo = Objects.nonNull(filterStudentRequest.getCurrentPage()) ? filterStudentRequest.getCurrentPage() : this.pageNo;
        this.pageSize = Objects.nonNull(filterStudentRequest.getTotalItemEachPage()) ? filterStudentRequest.getTotalItemEachPage() : this.getPageSize();
        return PageRequest.of(pageNo, pageSize, sortType, sortField);
    }
}
