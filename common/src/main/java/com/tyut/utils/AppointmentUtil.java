package com.tyut.utils;

import java.util.concurrent.ThreadLocalRandom;

public class AppointmentUtil {

    // 自定义 Base32 字符集（剔除了容易混淆的字符）
    private static final String ALPHABET = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final int ALPHABET_SIZE = ALPHABET.length();

    /**
     * 生成唯一预约号
     * * @param weekDay    1-7 代表周一到周日
     * @param timeSlot   "AM" 或 "PM"
     * @param doctorId   医生唯一ID
     * @param residentId 患者唯一ID
     * @return 格式如：2A-F8K-B9
     */
    public static String generateQueueNo(int weekDay, String timeSlot, long doctorId, long residentId) {
        // 1. 获取时段标识 (A/P)
        String slotKey = "AM".equalsIgnoreCase(timeSlot) ? "A" : "P";

        // 2. 构造复合因子 (通过位运算或乘法构造长整型种子)
        // 这里的系数选取是为了尽可能让各 ID 段在二进制/进制转换后分散开
        long seed = (doctorId % 10000) * 1000000L
                + (residentId % 10000) * 100L
                + ThreadLocalRandom.current().nextInt(10, 99);

        // 3. 将种子转换为自定义 Base32 编码
        String encoded = encodeBase32(seed);

        // 4. 格式化输出：[星期][时段]-[医生&患者混淆位]-[随机校验位]
        // 保证长度大致固定，不足位补随机字符
        if (encoded.length() < 5) {
            encoded = String.format("%-5s", encoded).replace(' ', 'X');
        }

        return String.format("%d%s-%s-%s",
                weekDay,
                slotKey,
                encoded.substring(0, 3),
                encoded.substring(encoded.length() - 2));
    }

    /**
     * 简单的进制转换逻辑
     */
    private static String encodeBase32(long num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(ALPHABET.charAt((int) (num % ALPHABET_SIZE)));
            num /= ALPHABET_SIZE;
        }
        return sb.reverse().toString();
    }

}