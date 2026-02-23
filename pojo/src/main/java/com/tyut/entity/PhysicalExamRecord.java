package com.tyut.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 体检记录实体类
 * 对应数据库表 physical_exam_record
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhysicalExamRecord {
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 身高(cm)
     */
    private BigDecimal height;
    
    /**
     * 体重(kg)
     */
    private BigDecimal weight;
    
    /**
     * 收缩压
     */
    private Integer systolicBp;
    
    /**
     * 舒张压
     */
    private Integer diastolicBp;
    
    /**
     * 心率
     */
    private Integer heartRate;
    
    /**
     * 血糖
     */
    private BigDecimal bloodSugar;
    
    /**
     * BMI指数
     */
    private BigDecimal bmi;
    
    /**
     * 体检时间
     */
    private LocalDateTime examTime;
    
    /**
     * 逻辑删除标识(0:正常 1:已删除)
     */
    private Integer isDeleted;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 关联的健康档案ID
     */
    private Long recordId;
}
