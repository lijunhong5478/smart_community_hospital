package com.tyut.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 健康档案查询DTO
 * 支持通过多种条件查询健康档案信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "健康档案查询DTO")
public class HealthRecordQueryDTO {
    
    @ApiModelProperty(value = "用户ID", example = "1")
    private Long userId;
    
    @ApiModelProperty(value = "用户真实姓名", example = "张三")
    private String realName;
    
    @ApiModelProperty(value = "用户身份证号", example = "110101199001011234")
    private String idCard;
    
    @ApiModelProperty(value = "用户电话号码", example = "13800138000")
    private String phone;
    
    @ApiModelProperty(value = "页码", example = "1")
    private Integer pageNum = 1;
    
    @ApiModelProperty(value = "每页大小", example = "10")
    private Integer pageSize = 10;
}