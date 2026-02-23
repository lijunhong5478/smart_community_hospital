package com.tyut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.constant.ConsultationConstant;
import com.tyut.context.BaseContext;
import com.tyut.dto.ConsultationQueryDTO;
import com.tyut.dto.SendMessageDTO;
import com.tyut.dto.StartConsultationDTO;
import com.tyut.entity.ConsultationMessage;
import com.tyut.entity.ConsultationSession;
import com.tyut.mapper.ConsultationMessageMapper;
import com.tyut.mapper.ConsultationSessionMapper;
import com.tyut.result.PageResult;
import com.tyut.service.ConsultationService;
import com.tyut.vo.ConsultationSessionVO;
import com.tyut.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultationServiceImpl implements ConsultationService {

    @Autowired
    private ConsultationSessionMapper consultationSessionMapper;

    @Autowired
    private ConsultationMessageMapper consultationMessageMapper;

    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    @Transactional
    public Long startConsultation(StartConsultationDTO dto) {
        ConsultationSession session = ConsultationSession.builder()
                .residentId(dto.getResidentId())
                .doctorId(dto.getDoctorId())
                .status(ConsultationConstant.PROCEEDING)
                .createTime(LocalDateTime.now())
                .build();

        consultationSessionMapper.insert(session);
        return session.getId();
    }

    @Override
    @Transactional
    public void sendMessage(SendMessageDTO dto) {
        ConsultationMessage message = ConsultationMessage.builder()
                .sessionId(dto.getSessionId())
                .senderType(dto.getSenderType())
                .senderId(dto.getSenderId())
                .messageType(dto.getMessageType())
                .content(dto.getContent())
                .duration(dto.getDuration())
                .isRead(0)
                .createTime(LocalDateTime.now())
                .build();

        consultationMessageMapper.insert(message);

        // 通过WebSocket推送消息
        sendWebSocketMessage(dto.getSessionId(), message);
    }

    @Override
    public PageResult listSessions(ConsultationQueryDTO queryDTO) {
        Page<ConsultationSession> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<ConsultationSession> queryWrapper = new LambdaQueryWrapper<>();

        // 使用BaseContext中的用户信息（来自JWT）
        Long currentUserId = BaseContext.getCurrentId();
        Integer currentUserRole = BaseContext.getCurrentRole();

        if (currentUserRole != null && currentUserId != null) {
            if (currentUserRole == 2) { // 居民
                queryWrapper.eq(ConsultationSession::getResidentId, currentUserId);
            } else if (currentUserRole == 1) { // 医生
                queryWrapper.eq(ConsultationSession::getDoctorId, currentUserId);
            }
        }

        if (queryDTO.getStatus() != null) {
            queryWrapper.eq(ConsultationSession::getStatus, queryDTO.getStatus());
        }

        queryWrapper.orderByDesc(ConsultationSession::getCreateTime);

        IPage<ConsultationSession> pageData = consultationSessionMapper.selectPage(page, queryWrapper);

        List<ConsultationSessionVO> voList = pageData.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageResult(pageData.getTotal(), voList);
    }

    @Override
    @Transactional
    public void endConsultation(Long sessionId) {
        ConsultationSession session = new ConsultationSession();
        session.setId(sessionId);
        session.setStatus(ConsultationConstant.FINISHED);
        consultationSessionMapper.updateById(session);
    }

    @Override
    @Transactional
    public void markAsRead(Long messageId) {
        ConsultationMessage message = new ConsultationMessage();
        message.setId(messageId);
        message.setIsRead(1);
        consultationMessageMapper.updateById(message);
    }

    /**
     * 通过WebSocket发送消息
     */
    private void sendWebSocketMessage(Long sessionId, ConsultationMessage message) {
        // 获取会话信息
        ConsultationSession session = consultationSessionMapper.selectById(sessionId);

        if (session == null) {
            return;
        }

        // 向医生和居民分别发送消息
        String doctorSid = "doctor_" + session.getDoctorId();
        String residentSid = "resident_" + session.getResidentId();

        // 构造消息内容
        String messageContent = buildMessageContent(message);

        // 检查用户是否在线再发送
        if (webSocketServer.isUserOnline("doctor_", session.getDoctorId())) {
            webSocketServer.sendToClient(doctorSid, messageContent);
        }

        if (webSocketServer.isUserOnline("resident_", session.getResidentId())) {
            webSocketServer.sendToClient(residentSid, messageContent);
        }
    }

    /**
     * 构造消息内容
     */
    private String buildMessageContent(ConsultationMessage message) {
        // 构造JSON格式的消息
        return "{"
                + "\"type\":\"message\","
                + "\"messageId\":" + message.getId() + ","
                + "\"sessionId\":" + message.getSessionId() + ","
                + "\"senderType\":" + message.getSenderType() + ","
                + "\"messageType\":" + message.getMessageType() + ","
                + "\"content\":\"" + escapeJsonString(message.getContent()) + "\","
                + "\"timestamp\":\"" + message.getCreateTime().toString() + "\""
                + "}";
    }

    /**
     * 转义JSON字符串
     */
    private String escapeJsonString(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    /**
     * 转换为VO对象
     */
    private ConsultationSessionVO convertToVO(ConsultationSession session) {
        ConsultationSessionVO vo = new ConsultationSessionVO();
        // 这里需要根据实际VO结构进行转换
        // 可以查询相关的用户信息、消息等
        vo.setId(session.getId());
        vo.setResidentId(session.getResidentId());
        vo.setDoctorId(session.getDoctorId());
        vo.setStatus(session.getStatus());
        vo.setCreateTime(session.getCreateTime());
        return vo;
    }
}
