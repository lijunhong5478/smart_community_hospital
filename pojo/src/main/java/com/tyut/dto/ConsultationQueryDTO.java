package com.tyut.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询会话DTO
 */
@Data
@ApiModel(description = "查询会话DTO")
public class ConsultationQueryDTO {

    @ApiModelProperty(value = "用户ID", example = "1")
    private Long userId;

    @ApiModelProperty(value = "用户类型 0居民 1医生", example = "0")
    private Integer userType;

    @ApiModelProperty(value = "会话状态 0进行中 1已结束", example = "0")
    private Integer status;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页大小", example = "10")
    private Integer pageSize = 10;
}
