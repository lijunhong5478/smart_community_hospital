package com.tyut.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 居民病史实体类
 * 对应数据库表 resident_medical_history
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResidentMedicalHistory {
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 关联的健康档案ID
     */
    private Long recordId;
    
    /**
     * 慢性病史
     */
    private String chronicDisease;
    
    /**
     * 既往病史
     */
    private String pastMedicalHistory;
    
    /**
     * 过敏史
     */
    private String allergyHistory;
    
    /**
     * 家族史
     */
    private String familyHistory;
    
    /**
     * 手术史
     */
    private String surgeryHistory;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 逻辑删除标识(0:正常 1:已删除)
     */
    private Integer isDeleted;
}