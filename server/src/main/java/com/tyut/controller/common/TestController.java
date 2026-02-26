package com.tyut.controller.common;

import dev.langchain4j.model.ollama.OllamaChatModel;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

/**
 * Langchain4j测试控制器
 */

@RestController
@RequestMapping("/api/test")
@Api(tags = "langchain4j测试")
public class TestController {

    private final OllamaChatModel chatModel;

    public TestController(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * 测试Ollama连接和基本功能
     * GET /api/test/hello
     */
    @GetMapping("/hello")
    public String hello() {
        return "Hello, LangChain4j is working!";
    }

    /**
     * 发送你好给Ollama模型
     * POST /api/test/send-hello
     */
    @PostMapping("/send-hello")
    public String sendHello() {
        try {
            // 直接发送"你好"给模型
            String response = chatModel.generate("你好");
            return "模型回复: " + response;
        } catch (Exception e) {
            return "错误: " + e.getMessage();
        }
    }

    /**
     * 测试模型基本信息
     * GET /api/test/info
     */
    @GetMapping("/info")
    public String getInfo() {
        return "Ollama模型: deepseek-r1:1.5b\n" +
                "LangChain4j版本: 0.35.0";
    }
}
