package com.tyut.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "疫情预警VO")
public class EpidemicAlertVO {

    @ApiModelProperty(value = "预警ID")
    private Long id;

    @ApiModelProperty(value = "地区")
    private String region;

    @ApiModelProperty(value = "风险等级")
    private Integer riskLevel;

    @ApiModelProperty(value = "预警信息")
    private String message;

    @ApiModelProperty(value = "发布时间")
    private LocalDateTime publishTime;
}
