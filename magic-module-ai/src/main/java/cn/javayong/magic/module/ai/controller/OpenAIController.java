package cn.javayong.magic.module.ai.controller;

import cn.javayong.magic.framework.common.util.json.JsonUtils;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatCompletions;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatRespCommand;
import cn.javayong.magic.module.ai.domain.AiOneApiTokenDO;
import cn.javayong.magic.module.ai.domain.vo.OpenAIChatReqVO;
import cn.javayong.magic.module.ai.service.AiOneApiTokenService;
import cn.javayong.magic.module.ai.service.OpenAIService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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

@Tag(name = "兼容 openai 的核心接口")
@RestController("OpenAIController")
@RequestMapping("/chat")
@Slf4j
public class OpenAIController {

    @Autowired
    private OpenAIService openAIService;

    /**
     * 统一接口支持两种类型
     * 创建文本对话请求 参考 deepseek 文档：  https://api-docs.deepseek.com/zh-cn/
     * 或者 硅基流动  https://docs.siliconflow.cn/cn/api-reference/chat-completions/chat-completions
     */
    @RequestMapping(value = "/completions",
            produces = {MediaType.TEXT_EVENT_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @PermitAll
    public void completions(@RequestBody OpenAIChatReqVO openAIChatReqVO,
                              @RequestHeader(value = "Authorization", required = false) String oneApiToken,
                              HttpServletResponse response) throws IOException {

        log.info("openAIChatReqVO:" + JsonUtils.toJsonString(openAIChatReqVO) + " oneApiToken:" + oneApiToken);

        // 流式
        if (openAIChatReqVO.isStream()) {
            response.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
            OpenAIChatRespCommand<Flux<String>> streamedRespCommand = openAIService.streamCompletions(openAIChatReqVO);
            Flux<String> dataStream = streamedRespCommand.getData();
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            // 关键点：切换到当前线程执行
            dataStream
                    .publishOn(Schedulers.immediate()) // 确保操作在当前线程
                    .doOnNext(data -> {
                        writer.write("data: " + data + "\n\n"); // SSE格式
                        writer.flush();
                    })
                    .doOnError(e -> {
                        writer.write("event: error\ndata: " + e.getMessage() + "\n\n");
                        writer.flush();
                    })
                    .doOnComplete(() -> {
                        writer.write("event: done\ndata: [DONE]\n\n");
                        writer.flush();
                        writer.close();
                    })
                    .blockLast(); // 阻塞直到流结束
        }

        // 非流式
        else {
            OpenAIChatRespCommand<OpenAIChatCompletions> blockRespCommand = openAIService.blockCompletions(openAIChatReqVO);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(JsonUtils.toJsonString(blockRespCommand.getData()));
            writer.flush();
            writer.close();
        }

    }

}