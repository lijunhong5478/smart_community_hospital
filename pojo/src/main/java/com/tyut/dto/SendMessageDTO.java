package com.tyut.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发送消息DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "发送消息DTO")
public class SendMessageDTO {

    @ApiModelProperty(value = "会话ID", example = "1")
    private Long sessionId;

    @ApiModelProperty(value = "发送者类型 0居民 1医生", example = "0")
    private Integer senderType;

    @ApiModelProperty(value = "发送者ID", example = "1")
    private Long senderId;

    @ApiModelProperty(value = "消息类型 0文本 1图片 2语音", example = "0")
    private Integer messageType;

    @ApiModelProperty(value = "文本内容或文件URL")
    private String content;

    @ApiModelProperty(value = "语音时长(秒)", example = "30")
    private Integer duration;
}
