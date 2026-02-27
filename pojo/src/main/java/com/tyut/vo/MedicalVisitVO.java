package com.tyut.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalVisitVO {
    private Long id;
    private String doctorName;
    private String doctorPhone;
    private String doctorImage;
    private Integer doctorTitle;
    private String doctorDepartment;
    private String residentName;
    private String chiefComplaint;
    private String treatmentAdvice;
    private LocalDateTime createTime;
}
