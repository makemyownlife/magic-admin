package cn.javayong.magic.module.ai.adapter.platform;

import cn.javayong.magic.framework.common.util.json.JsonUtils;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatReqCommand;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatCompletions;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatRespCommand;
import cn.javayong.magic.module.ai.adapter.core.AiPlatformChatClient;
import cn.javayong.magic.module.ai.adapter.core.AiPlatformConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 标准 DeepSeek 供应商 API ，兼容 openai 协议
 */
@Slf4j
public class DeepSeekChatClient implements AiPlatformChatClient {

    private static final String CHAT_COMPLETIONS_ENDPOINT = "/chat/completions";

    private AiPlatformConfig aiSupplierConfig;

    @Override
    public void init(AiPlatformConfig aiSupplierConfig) {
        this.aiSupplierConfig = aiSupplierConfig;
    }

    public OpenAIChatRespCommand<Flux<String>> streamChatCompletion(OpenAIChatReqCommand openAIChatReqCommand) {
        OpenAIChatRespCommand<Flux<String>> respCommand = new OpenAIChatRespCommand<Flux<String>>();
        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();

        CountDownLatch countDownLatch = new CountDownLatch(1);  // 用于阻塞，当返回 HTTP code = 200 才读取流，本来可以直接返回的，但还是细粒度的控制一下，看看后面有没有更好的方式。
        try {
            // 1. 创建 WebClient
            WebClient client = WebClient.builder()
                    .baseUrl(aiSupplierConfig.getBaseUrl())
                    .defaultHeader("Authorization", "Bearer " + aiSupplierConfig.getApiKey())
                    .build();

            // 2. 发送流式请求（阻塞直到响应完成）
            client.post()
                    .uri(CHAT_COMPLETIONS_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.TEXT_EVENT_STREAM)
                    .bodyValue(JsonUtils.toJsonString(openAIChatReqCommand))
                    .retrieve()
                    .onStatus(
                            status -> {
                                if (status == HttpStatus.OK) {
                                    respCommand.setCode(OpenAIChatRespCommand.SUCCESS_CODE);
                                } else {
                                    respCommand.setCode(OpenAIChatRespCommand.INTERNAL_ERROR_CODE);
                                }
                                countDownLatch.countDown();
                                return status != HttpStatus.OK;  // 非 200 时触发错误处理
                            },
                            response -> response.bodyToMono(String.class)
                                    .defaultIfEmpty("No error details")
                                    .flatMap(errorBody -> {
                                        respCommand.setMessage(errorBody);
                                        log.error("API Error (HTTP {}): {}", response.statusCode(), errorBody);
                                        return Mono.error(new RuntimeException(
                                                "API Error: HTTP " + response.statusCode() + " - " + errorBody
                                        ));
                                    })
                    )
                    .bodyToFlux(String.class)
                    .subscribe(
                            data -> {
                                // 3 在接收到数据后，实时推送到 Sinks
                                sink.tryEmitNext(data);
                            },
                            error -> {
                                log.error("SSE Stream Error: ", error);
                                sink.tryEmitError(error);
                            },
                            () -> {
                                sink.tryEmitComplete();
                            }
                    );

            // 阻塞，直到接收到 HTTP 响应码
            countDownLatch.await(5000L, TimeUnit.MILLISECONDS);  // 在这里进行阻塞，直到 Sinks 完成处理

            // 将 Sinks 转为 Flux
            respCommand.setData(sink.asFlux());
            return respCommand;
        } catch (Exception e) {
            log.error("Deepseek invoke api error:", e);
            respCommand.setCode(OpenAIChatRespCommand.INTERNAL_ERROR_CODE);
            return respCommand;
        }
    }

    @Override
    public OpenAIChatRespCommand<OpenAIChatCompletions> blockChatCompletion(OpenAIChatReqCommand openAIChatReqCommand) {
        OpenAIChatRespCommand respCommand = new OpenAIChatRespCommand();
        try {
            // 1. 创建 WebClient
            WebClient client = WebClient.builder()
                    .baseUrl(aiSupplierConfig.getBaseUrl())
                    .defaultHeader("Authorization", "Bearer " + aiSupplierConfig.getApiKey())
                    .build();

            // 2. 发送阻塞请求并处理 JSON 响应
            OpenAIChatCompletions completions =
                    client.post()
                            .uri(CHAT_COMPLETIONS_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .bodyValue(JsonUtils.toJsonString(openAIChatReqCommand))
                            .retrieve()
                            .bodyToMono(OpenAIChatCompletions.class)
                            .block();

            respCommand.setCode(OpenAIChatRespCommand.SUCCESS_CODE);
            respCommand.setData(completions);
            return respCommand;
        } catch (Exception e) {
            log.error("Deepseek blockChatCompletion invoke error:", e);
            respCommand.setCode(OpenAIChatRespCommand.INTERNAL_ERROR_CODE);
            respCommand.setMessage("调用 deepseek 官网API服务异常，请确保 API KEY 正确 和网络正常!");
        }
        return respCommand;
    }

    public static void main(String[] args) {
        AiPlatformConfig aiPlatformConfig = new AiPlatformConfig();
        aiPlatformConfig.setBaseUrl("https://api.deepseek.com/v1/");
        aiPlatformConfig.setApiKey("sk-31da87a7c6eb40188fb1a71f98fa6fbd");

        AiPlatformChatClient aiPlatformChatClient = new DeepSeekChatClient();
        aiPlatformChatClient.init(aiPlatformConfig);

        OpenAIChatReqCommand openAIChatReqCommand = new OpenAIChatReqCommand();
        openAIChatReqCommand.setModel("deepseek-chat");
        openAIChatReqCommand.setStream(true);
        openAIChatReqCommand.setTemperature(0.7);

        // 添加消息列表
        List<OpenAIChatReqCommand.ChatMessage> chatMessageList = new ArrayList<>();
        OpenAIChatReqCommand.ChatMessage chatMessage = new OpenAIChatReqCommand.ChatMessage("user", "Hello, how are you?");
        chatMessageList.add(chatMessage);
        openAIChatReqCommand.setMessages(chatMessageList);

        OpenAIChatRespCommand<Flux<String>> respCommand = aiPlatformChatClient.streamChatCompletion(openAIChatReqCommand);

        respCommand.getData().subscribe(
                line -> System.out.println(line),       // onNext
                error -> System.err.println(error),     // onError
                () -> System.out.println("Stream completed!")  // onComplete
        );

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
