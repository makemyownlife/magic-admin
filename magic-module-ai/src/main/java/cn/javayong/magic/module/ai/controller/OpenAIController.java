package cn.javayong.magic.module.ai.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.security.PermitAll;
import java.time.Duration;

@Tag(name = "管理后台 - 验证码")
@RestController("OpenAIController")
@RequestMapping("/ai/admin")
@Slf4j
public class OpenAIController {

    /**
     * 创建文本对话请求 参考 硅基流动  https://docs.siliconflow.cn/cn/api-reference/chat-completions/chat-completions
     */
    @RequestMapping(value = "/chat/completions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PermitAll
    public Flux<ServerSentEvent<String>> streamCustomEvents() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> ServerSentEvent.<String>builder()
                        .id(String.valueOf(sequence))
                        .event("custom-event")
                        .data("Event #" + sequence)
                        .build());
    }
    

}
