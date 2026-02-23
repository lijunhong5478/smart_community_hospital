package com.tyut.context;

/**
 * WebSocket上下文类
 * 用于存储WebSocket连接中的用户信息
 */
public class WebSocketContext {

    private static final ThreadLocal<Long> USER_ID_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<Integer> USER_TYPE_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<String> SESSION_ID_HOLDER = new ThreadLocal<>();

    /**
     * 设置用户ID
     */
    public static void setUserId(Long userId) {
        USER_ID_HOLDER.set(userId);
    }

    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        return USER_ID_HOLDER.get();
    }

    /**
     * 设置用户类型
     */
    public static void setUserType(Integer userType) {
        USER_TYPE_HOLDER.set(userType);
    }

    /**
     * 获取用户类型
     */
    public static Integer getUserType() {
        return USER_TYPE_HOLDER.get();
    }

    /**
     * 设置会话ID
     */
    public static void setSessionId(String sessionId) {
        SESSION_ID_HOLDER.set(sessionId);
    }

    /**
     * 获取会话ID
     */
    public static String getSessionId() {
        return SESSION_ID_HOLDER.get();
    }

    /**
     * 清除所有上下文信息
     */
    public static void remove() {
        USER_ID_HOLDER.remove();
        USER_TYPE_HOLDER.remove();
        SESSION_ID_HOLDER.remove();
    }
}
