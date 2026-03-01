package com.tyut.aop;

import com.tyut.annotation.DataBackUp;
import com.tyut.context.BaseContext;
import com.tyut.entity.OperationLog;
import com.tyut.mapper.OperationLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Aspect
@Slf4j
public class OperationLogAspect {
    @Autowired
    private OperationLogMapper operationLogMapper;
    @Pointcut("execution(* com.tyut.service..*.*(..)) && @annotation(com.tyut.annotation.DataBackUp)")
    public void dataBackUpPointcut() {
    }
    /**
     * 环绕通知：在方法执行前后记录操作日志
     */
    @Around("dataBackUpPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getMethod().getName();

        // 获取注解信息
        DataBackUp dataBackUp = signature.getMethod().getAnnotation(DataBackUp.class);
        String moduleName = dataBackUp.module().getDescription();
        // 获取当前用户信息
        Long userId = null;
        Integer roleType = null;
        try {
            userId = BaseContext.getCurrentId();
            roleType = BaseContext.getCurrentRole();
        } catch (Exception e) {
            log.warn("获取用户上下文信息失败", e);
        }

        // 创建操作日志
        OperationLog operationLog = OperationLog.builder()
                .userId(userId)
                .roleType(roleType)
                .methodName(methodName)
                .moduleName(moduleName)
                .createTime(LocalDateTime.now())
                .build();

        try {
            // 执行目标方法
            Object result = joinPoint.proceed();

            // 插入操作日志
            operationLogMapper.insert(operationLog);

            return result;
        } catch (Throwable throwable) {
            // 异常情况下也记录日志
            log.error("方法执行异常: {}", methodName, throwable);
            operationLog.setModuleName(moduleName + " [异常]");
            operationLogMapper.insert(operationLog);
            throw throwable;
        }
    }
}
