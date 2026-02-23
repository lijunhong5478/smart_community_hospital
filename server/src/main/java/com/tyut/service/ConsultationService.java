package com.tyut.service;

import com.tyut.dto.ConsultationQueryDTO;
import com.tyut.dto.SendMessageDTO;
import com.tyut.dto.StartConsultationDTO;
import com.tyut.result.PageResult;
import com.tyut.vo.ConsultationSessionVO;

public interface ConsultationService {

    /**
     * 开始新的咨询会话
     */
    Long startConsultation(StartConsultationDTO dto);

    /**
     * 发送消息
     */
    void sendMessage(SendMessageDTO dto);

    /**
     * 查询会话列表
     */
    PageResult listSessions(ConsultationQueryDTO queryDTO);

    /**
     * 结束会话
     */
    void endConsultation(Long sessionId);

    /**
     * 标记消息为已读
     */
    void markAsRead(Long messageId);
}
