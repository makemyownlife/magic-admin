package cn.javayong.magic.module.ai.controller;

import cn.javayong.magic.framework.common.pojo.CommonResult;
import cn.javayong.magic.framework.common.util.servlet.ServletUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
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
    @RequestMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PermitAll
    public SseEmitter chatDemo() {
        SseEmitter emitter = new SseEmitter();
        try {
            for (int i = 0; i < 10; i++) {
                SseEmitter.SseEventBuilder event = SseEmitter.event()
                        .name("message")
                        .data("SSE消息 " + i);
                emitter.send(event);
                Thread.sleep(1000);
            }
            emitter.complete();
        } catch (Exception e) {
            emitter.completeWithError(e);
        }
        return emitter;
    }

}
