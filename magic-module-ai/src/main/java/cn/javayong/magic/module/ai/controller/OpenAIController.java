package cn.javayong.magic.module.ai.controller;

import cn.javayong.magic.framework.common.util.json.JsonUtils;
import cn.javayong.magic.module.ai.adapter.AISupplierChatClient;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatReqCommand;
import cn.javayong.magic.module.ai.adapter.impl.DeepSeekAISupplierChatClient;
import cn.javayong.magic.module.ai.domain.convert.ChatConvert;
import cn.javayong.magic.module.ai.domain.vo.OpenAIChatReqVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.security.PermitAll;

@Tag(name = "兼容 openai 的核心接口")
@RestController("OpenAIController")
@RequestMapping("/chat")
@Slf4j
public class OpenAIController {

    /**
     * 创建文本对话请求 参考 硅基流动  https://docs.siliconflow.cn/cn/api-reference/chat-completions/chat-completions
     */
    @RequestMapping(value = "/completions",
            produces = {MediaType.TEXT_EVENT_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @PermitAll
    public Flux completions(@RequestBody OpenAIChatReqVO openAIChatReqVO,
                            @RequestHeader(value = "Accept", required = false) String acceptHeader) {

        log.info("openAIChatReqVO:" + JsonUtils.toJsonString(openAIChatReqVO));

        OpenAIChatReqCommand openAIChatReqCommand = ChatConvert.INSTANCE.convert(openAIChatReqVO);

        AISupplierChatClient aiSupplierChatClient = new DeepSeekAISupplierChatClient();
        Flux<String> result = aiSupplierChatClient.chatCompletion(openAIChatReqCommand);

        if (openAIChatReqCommand.isStream()) {
            // 默认返回SSE流
            return result.map(data -> ServerSentEvent.builder(data).build());
        } else {
            return result;
        }
    }

}
