package cn.javayong.magic.module.ai.controller;

import cn.javayong.magic.framework.common.util.json.JsonUtils;
import cn.javayong.magic.module.ai.adapter.AISupplierChatClient;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatReqCommand;
import cn.javayong.magic.module.ai.adapter.impl.DeepSeekAISupplierChatClient;
import cn.javayong.magic.module.ai.domain.convert.ChatConvert;
import cn.javayong.magic.module.ai.domain.vo.OpenAIChatReqVO;
import cn.javayong.magic.module.ai.service.OpenAIService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.security.PermitAll;

@Tag(name = "兼容 openai 的核心接口")
@RestController("OpenAIController")
@RequestMapping("/chat")
@Slf4j
public class OpenAIController {

    @Autowired
    private OpenAIService openAIService;

    /**
     * 创建文本对话请求 参考 硅基流动  https://docs.siliconflow.cn/cn/api-reference/chat-completions/chat-completions
     */
    @RequestMapping(value = "/completions",
            produces = {MediaType.TEXT_EVENT_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @PermitAll
    public Object completions(@RequestBody OpenAIChatReqVO openAIChatReqVO) {

        log.info("openAIChatReqVO:" + JsonUtils.toJsonString(openAIChatReqVO));

        return openAIService.completions(openAIChatReqVO);
    }

}