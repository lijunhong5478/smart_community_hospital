package com.tyut.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@ApiModel(description = "操作日志查询DTO")
public class OperationLogQueryDTO {

    private Integer pageSize;

    private Integer pageNum;

    @ApiModelProperty(value = "开始日期", example = "2024-01-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty(value = "结束日期", example = "2024-12-31")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    /**
     * 初始化默认值
     * 如果startDate为null，则设置为2022年9月18日
     * 如果endDate为null，则设置为今天
     */
    public void initDefaultValues() {
        if (this.startDate == null) {
            this.startDate = LocalDate.of(2022, 9, 18);
        }
        if (this.endDate == null) {
            this.endDate = LocalDate.now();
        }
    }
}
