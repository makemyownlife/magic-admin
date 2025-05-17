package cn.javayong.magic.module.ai.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类似 oneapi 提供 token 管理的接口
 */
@Tag(name = "兼容 openai 的核心接口")
@RestController("AdminAiTokenContoller")
@RequestMapping("/ai/oneapi/")
@Slf4j
public class AdminAiTokenContoller {
    
}
