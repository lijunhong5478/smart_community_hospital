package com.tyut.controller.resident;

import com.tyut.dto.ConsultationQueryDTO;
import com.tyut.dto.SendMessageDTO;
import com.tyut.dto.StartConsultationDTO;
import com.tyut.result.PageResult;
import com.tyut.result.Result;
import com.tyut.service.ConsultationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resident/consultation")
@Api(tags = "居民-咨询接口")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    @PostMapping("/start")
    @ApiOperation("开始咨询")
    public Result<Long> startConsultation(@RequestBody StartConsultationDTO dto) {
        Long sessionId = consultationService.startConsultation(dto);
        return Result.success(sessionId);
    }

    @PostMapping("/message")
    @ApiOperation("发送消息")
    public Result<String> sendMessage(@RequestBody SendMessageDTO dto) {
        consultationService.sendMessage(dto);
        return Result.success();
    }

    @GetMapping("/sessions")
    @ApiOperation("查询会话列表")
    public Result<PageResult> listSessions(ConsultationQueryDTO queryDTO) {
        return Result.success(consultationService.listSessions(queryDTO));
    }

    @PutMapping("/end/{sessionId}")
    @ApiOperation("结束会话")
    public Result<String> endConsultation(@PathVariable Long sessionId) {
        consultationService.endConsultation(sessionId);
        return Result.success();
    }

    @PutMapping("/read/{messageId}")
    @ApiOperation("标记消息为已读")
    public Result<String> markAsRead(@PathVariable Long messageId) {
        consultationService.markAsRead(messageId);
        return Result.success();
    }
}
