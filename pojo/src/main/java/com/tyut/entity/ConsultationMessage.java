package com.tyut.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 咨询消息实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationMessage {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 会话ID
     */
    private Long sessionId;

    /**
     * 发送者类型 0居民 1医生
     */
    private Integer senderType;

    /**
     * 发送者ID
     */
    private Long senderId;

    /**
     * 消息类型 0文本 1图片 2语音
     */
    private Integer messageType;

    /**
     * 文本内容或文件URL
     */
    private String content;

    /**
     * 语音时长(秒)
     */
    private Integer duration;

    /**
     * 是否已读 0未读 1已读
     */
    private Integer isRead;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
