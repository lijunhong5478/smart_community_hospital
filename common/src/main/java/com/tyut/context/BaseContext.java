package com.tyut.context;

public class BaseContext {

    private static final ThreadLocal<UserContext> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 设置当前用户
     */
    public static void set(UserContext userContext) {
        THREAD_LOCAL.set(userContext);
    }

    /**
     * 获取当前用户
     */
    public static UserContext get() {
        return THREAD_LOCAL.get();
    }

    /**
     * 获取当前用户ID
     */
    public static Long getCurrentId() {
        UserContext context = THREAD_LOCAL.get();
        return context != null ? context.getId() : null;
    }

    /**
     * 获取当前角色
     */
    public static Integer getCurrentRole() {
        UserContext context = THREAD_LOCAL.get();
        return context != null ? context.getRole() : null;
    }

    /**
     * 清除当前用户（必须调用）
     */
    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
