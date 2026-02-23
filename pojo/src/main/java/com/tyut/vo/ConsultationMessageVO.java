package com.tyut.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConsultationMessageVO {
    private Long id;
    private Long sessionId;
    private Integer senderType;
    private Long senderId;
    private String senderName;
    private Integer messageType;
    private String content;
    private Integer duration;
    private Integer isRead;
    private LocalDateTime createTime;
}
