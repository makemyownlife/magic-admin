package cn.javayong.magic.module.ai.controller;

import cn.javayong.magic.module.ai.adapter.AISupplierChatClient;
import cn.javayong.magic.module.ai.adapter.impl.DeepSeekAISupplierChatClient;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import javax.annotation.security.PermitAll;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Tag(name = "用户端-智能聊天")
@RestController("AppChatController")
@RequestMapping("/ai/app")
@Slf4j
public class AppChatController {

    //最简单的线程池
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * 简单的 SSE 代码例子
     */
    @RequestMapping(value = "/chatDemo1", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PermitAll
    public SseEmitter chatDemo12() {
        SseEmitter emitter = new SseEmitter();

        // 此处必须是线程池，否则页面会阻塞
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        SseEmitter.SseEventBuilder event = SseEmitter.event()
                                .name("message")
                                .data("SSE消息 " + i);
                        emitter.send(event);
                        Thread.sleep(1000);
                    }
                    // 最后调用 complete 方法
                    emitter.complete();
                } catch (Exception e) {
                    emitter.completeWithError(e);
                }
            }
        });

        return emitter;
    }

    @RequestMapping(value = "/chatDemo2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
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
