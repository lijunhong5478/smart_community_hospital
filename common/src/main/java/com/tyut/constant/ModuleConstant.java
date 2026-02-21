package com.tyut.constant;

public enum ModuleConstant {
    USER("用户管理"),
    DOCTOR("医生管理"),
    RESIDENT("居民管理"),
    APPOINTMENT("预约管理"),
    CONSULTATION("问诊管理"),
    HEALTH_RECORD("健康档案管理"),
    SYSTEM("系统管理");

    private final String description;

    ModuleConstant(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
