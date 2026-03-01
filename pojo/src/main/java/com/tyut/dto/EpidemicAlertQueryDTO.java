package com.tyut.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "疫情预警查询DTO")
public class EpidemicAlertQueryDTO {

    @ApiModelProperty(value = "页码", example = "1")
    private Integer page = 1;

    @ApiModelProperty(value = "每页大小", example = "10")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "地区（模糊查询）")
    private String region;

    @ApiModelProperty(value = "风险等级")
    private Integer riskLevel;

    @ApiModelProperty(value = "开始发布时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束发布时间")
    private LocalDateTime endTime;
}
