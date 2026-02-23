package com.tyut.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 开始咨询DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "开始咨询DTO")
public class StartConsultationDTO {

    @ApiModelProperty(value = "居民ID", example = "1")
    private Long residentId;

    @ApiModelProperty(value = "医生ID", example = "2")
    private Long doctorId;
}
