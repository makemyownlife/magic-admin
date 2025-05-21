package cn.javayong.magic.module.ai.controller;

import cn.javayong.magic.framework.common.util.json.JsonUtils;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatCompletions;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatRespCommand;
import cn.javayong.magic.module.ai.domain.AiOneApiTokenDO;
import cn.javayong.magic.module.ai.domain.vo.OpenAIChatReqVO;
import cn.javayong.magic.module.ai.service.AiOneApiTokenService;
import cn.javayong.magic.module.ai.service.OpenAIService;
import cn.javayong.magic.module.ai.util.ResponseUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Objects;

import static cn.javayong.magic.framework.security.core.util.SecurityFrameworkUtils.AUTHORIZATION_BEARER;

@Tag(name = "兼容 openai 的核心接口")
@RestController("OpenAIController")
@RequestMapping("/chat")
@Slf4j
public class OpenAIController {

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private AiOneApiTokenService aiOneApiTokenService;

    /**
     * 统一接口支持两种类型
     * 创建文本对话请求 参考 deepseek 文档：  https://api-docs.deepseek.com/zh-cn/
     * 或者 硅基流动  https://docs.siliconflow.cn/cn/api-reference/chat-completions/chat-completions
     */
    @RequestMapping(value = "/completions",
            produces = {MediaType.TEXT_EVENT_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @PermitAll
    public void completions(@RequestBody OpenAIChatReqVO openAIChatReqVO,
                            @RequestHeader(value = "Authorization", required = false) String authorization,
                            HttpServletResponse response) throws IOException {

        log.info("openAIChatReqVO:" + JsonUtils.toJsonString(openAIChatReqVO) + " authorization:" + authorization);

        // 1. 获取 token
        int index = authorization.indexOf(AUTHORIZATION_BEARER + " ");
        String token = index >= 0 ? authorization.substring(index + 7).trim() : authorization;

        // 2. 验证 token 是否存在
        AiOneApiTokenDO aiOneApiTokenDO = aiOneApiTokenService.getOneApiTokenByToken(token);
        if (aiOneApiTokenDO == null || aiOneApiTokenDO.getDeleted() || aiOneApiTokenDO.getExpireTime().isBefore(LocalDateTime.now())) {
            ResponseUtils.writeUnauthorized(response, "Unauthorized: Invalid API Key");
            return;
        }

        // 流式 SSE 模式
        if (openAIChatReqVO.isStream()) {
            OpenAIChatRespCommand<Flux<String>> streamedRespCommand = openAIService.streamCompletions(openAIChatReqVO);
            if (Objects.equals(streamedRespCommand.getCode(), OpenAIChatRespCommand.SUCCESS_CODE)) {
                Flux<String> dataStream = streamedRespCommand.getData();
                ResponseUtils.writeSSE(response, dataStream);
            } else {
                // 流式出现异常，返回 JSON 格式
                ResponseUtils.writeJSON(response, JsonUtils.toJsonString(streamedRespCommand));
            }
        }

        // 非流式 模式
        else {
            OpenAIChatRespCommand<OpenAIChatCompletions> blockRespCommand = openAIService.blockCompletions(openAIChatReqVO);
            if (Objects.equals(blockRespCommand.getCode(), OpenAIChatRespCommand.SUCCESS_CODE)) {
                ResponseUtils.writeJSON(response, JsonUtils.toJsonString(blockRespCommand.getData()));
            } else {
                // 非流式请求出现异常，返回 JSON 格式
                ResponseUtils.writeJSON(response, JsonUtils.toJsonString(blockRespCommand));
            }
        }

    }

}