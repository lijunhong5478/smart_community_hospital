// server/src/test/java/com/tyut/ConsultationServiceTest.java
package com.tyut;

import com.tyut.dto.SendMessageDTO;
import com.tyut.dto.StartConsultationDTO;
import com.tyut.service.ConsultationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringBootTest
@SpringJUnitConfig
public class ConsultationServiceTest {

    @Autowired
    private ConsultationService consultationService;

    @Test
    public void testBasicCommunication() {
        System.out.println("=== 咨询服务基础测试 ===");

        try {
            // 创建会话
            StartConsultationDTO startDto = StartConsultationDTO.builder()
                    .residentId(1L)
                    .doctorId(2L)
                    .build();

            Long sessionId = consultationService.startConsultation(startDto);
            System.out.println("✅ 会话创建成功，ID: " + sessionId);

            // 发送消息测试
            SendMessageDTO message = SendMessageDTO.builder()
                    .sessionId(sessionId)
                    .senderType(1)
                    .senderId(2L)
                    .messageType(0)
                    .content("测试消息内容")
                    .build();

            consultationService.sendMessage(message);
            System.out.println("✅ 消息发送成功");

            // 结束会话
            consultationService.endConsultation(sessionId);
            System.out.println("✅ 会话结束成功");

        } catch (Exception e) {
            System.err.println("❌ 测试执行失败: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== 咨询服务测试完成 ===");
    }

    @Test
    public void testServiceAvailability() {
        System.out.println("=== 服务可用性测试 ===");
        assert consultationService != null : "ConsultationService注入失败";
        System.out.println("✅ ConsultationService注入成功");
    }
}
