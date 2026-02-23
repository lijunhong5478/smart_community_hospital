package com.tyut.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ConsultationSessionVO {
    private Long id;
    private Long residentId;
    private String residentName;
    private Long doctorId;
    private String doctorName;
    private Integer status;
    private LocalDateTime createTime;
    private List<ConsultationMessageVO> messages;
}
