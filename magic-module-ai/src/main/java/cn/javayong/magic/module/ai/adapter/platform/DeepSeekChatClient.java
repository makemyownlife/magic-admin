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
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 标准 DeepSeek 供应商 API ，兼容 openai 协议
 */
@Slf4j
public class DeepSeekChatClient implements AiPlatformChatClient {

    private static final String CHAT_COMPLETIONS_ENDPOINT = "/chat/completions";

    private AiPlatformConfig aiSupplierConfig;

    public DeepSeekChatClient(AiPlatformConfig aiPlatformConfig) {
        this.aiSupplierConfig = aiPlatformConfig;
    }

    public OpenAIChatRespCommand<Flux<String>> streamChatCompletion(OpenAIChatReqCommand openAIChatReqCommand) {
        final String requestId = openAIChatReqCommand.getRequestId(); // 唯一请求标识
        OpenAIChatRespCommand<Flux<String>> respCommand = new OpenAIChatRespCommand<>();
        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        try {
            String requestBody = JsonUtils.toJsonString(openAIChatReqCommand);
            log.info("[{}] Starting stream request to {} | Headers: Authorization: Bearer ***** | Request: {}",
                    requestId, CHAT_COMPLETIONS_ENDPOINT, requestBody);

            WebClient client = WebClient.builder()
                    .baseUrl(aiSupplierConfig.getBaseUrl())
                    .defaultHeader("Authorization", "Bearer " + aiSupplierConfig.getApiKey())
                    .build();

            client.post()
                    .uri(CHAT_COMPLETIONS_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.TEXT_EVENT_STREAM)
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(
                            status -> {
                                boolean isError = status != HttpStatus.OK;
                                log.info("[{}] Received HTTP status: {} | Is error: {}",
                                        requestId, status.value(), isError);
                                respCommand.setCode(isError ?
                                        OpenAIChatRespCommand.INTERNAL_ERROR_CODE :
                                        OpenAIChatRespCommand.SUCCESS_CODE);
                                countDownLatch.countDown();
                                return isError;
                            },
                            response -> {
                                return response.bodyToMono(String.class)
                                        .defaultIfEmpty("No error details")
                                        .flatMap(errorBody -> {
                                            log.error("[{}] API Error Response | HTTP {} | Body: {}",
                                                    requestId, response.statusCode(), errorBody);
                                            respCommand.setMessage(errorBody);
                                            return Mono.error(new RuntimeException(
                                                    "HTTP " + response.statusCode() + " - " + errorBody
                                            ));
                                        });
                            }
                    )
                    .bodyToFlux(String.class)
                    .subscribe(
                            data -> {
                                log.info("[{}] Received SSE data: {}", requestId, data);
                                sink.tryEmitNext(data);
                            },
                            error -> {
                                log.error("[{}] SSE Stream Error: ", requestId, error);
                                sink.tryEmitError(error);
                            },
                            () -> {
                                log.info("[{}] SSE Stream completed successfully", requestId);
                                sink.tryEmitComplete();
                            }
                    );

            // 等待初始响应（5秒超时）
            if (!countDownLatch.await(5000L, TimeUnit.MILLISECONDS)) {
                log.warn("[{}] Timeout waiting for initial response", requestId);
            }

            respCommand.setData(sink.asFlux().doOnSubscribe(sub ->
                    log.info("[{}] Client subscribed to response stream", requestId)
            ));

            log.info("[{}] Returning response with code: {}", requestId, respCommand.getCode());
            return respCommand;
        } catch (Exception e) {
            log.error("[{}] Exception during API call: ", requestId, e);
            respCommand.setCode(OpenAIChatRespCommand.INTERNAL_ERROR_CODE);
            respCommand.setMessage(e.getMessage());
            sink.tryEmitError(e); // 确保异常传播到流
            return respCommand;
        }
    }

    @Override
    public OpenAIChatRespCommand<OpenAIChatCompletions> blockChatCompletion(OpenAIChatReqCommand openAIChatReqCommand) {
        OpenAIChatRespCommand<OpenAIChatCompletions> respCommand = new OpenAIChatRespCommand<OpenAIChatCompletions>();
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

        AiPlatformChatClient aiPlatformChatClient = new DeepSeekChatClient(aiPlatformConfig);

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
