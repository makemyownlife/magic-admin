package cn.javayong.magic.module.ai.adapter.platform;

import cn.javayong.magic.framework.common.util.json.JsonUtils;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatCompletions;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatReqCommand;
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
 * 字节豆包 AI 供应商 API 实现
 */
@Slf4j
public class DouBaoChatClient implements AiPlatformChatClient {

    private static final String CHAT_COMPLETIONS_ENDPOINT = "/chat/completions";

    private AiPlatformConfig aiPlatformConfig;

    public DouBaoChatClient(AiPlatformConfig aiPlatformConfig) {
        this.aiPlatformConfig = aiPlatformConfig;
    }

    @Override
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
                    .baseUrl(aiPlatformConfig.getBaseUrl())
                    .defaultHeader("Authorization", "Bearer " + aiPlatformConfig.getApiKey())
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
        OpenAIChatRespCommand respCommand = new OpenAIChatRespCommand();
        try {
            // 1. 创建 WebClient
            WebClient client = WebClient.builder()
                    .baseUrl(aiPlatformConfig.getBaseUrl())
                    .defaultHeader("Authorization", "Bearer " + aiPlatformConfig.getApiKey())
                    .build();

            // 2. 发送阻塞请求并处理 JSON 响应
            OpenAIChatCompletions completions = client.post()
                    .uri(CHAT_COMPLETIONS_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(JsonUtils.toJsonString(openAIChatReqCommand))
                    .exchangeToMono(response -> {
                        HttpStatus status = response.statusCode();
                        if (status == HttpStatus.OK) {
                            return response.bodyToMono(OpenAIChatCompletions.class)
                                    .doOnNext(body -> log.info("API调用成功，返回值: {}", JsonUtils.toJsonString(body)));
                        } else {
                            return response.bodyToMono(String.class)
                                    .defaultIfEmpty("") // 如果响应体为空，使用空字符串
                                    .flatMap(body -> {
                                        log.error("API调用失败，HTTP状态码: {}，错误响应: {}", status.value(), body);
                                        return Mono.error(new RuntimeException("API调用失败，状态码: " + status.value() + "，错误信息: " + body));
                                    });
                        }
                    })
                    .block();

            respCommand.setCode(OpenAIChatRespCommand.SUCCESS_CODE);
            respCommand.setData(completions);
            return respCommand;
        } catch (Exception e) {
            log.error("DouBao blockChatCompletion invoke error:", e);
            respCommand.setCode(OpenAIChatRespCommand.INTERNAL_ERROR_CODE);
            // 如果异常是RuntimeException且包含API错误信息，则使用该信息
            if (e instanceof RuntimeException && e.getMessage() != null && e.getMessage().contains("API调用失败")) {
                respCommand.setMessage(e.getMessage());
            } else {
                respCommand.setMessage("调用豆包官网API服务异常，请确保 API KEY 正确和网络正常!");
            }
        }
        return respCommand;
    }

    public static void main(String[] args) throws InterruptedException {

        AiPlatformConfig aiPlatformConfig = new AiPlatformConfig();
        aiPlatformConfig.setBaseUrl("https://ark.cn-beijing.volces.com/api/v3/");
        aiPlatformConfig.setApiKey("11515f06-c8fe-4532-83b8-7d5145bd3132");

        AiPlatformChatClient aiSupplierChatClient = new DouBaoChatClient(aiPlatformConfig);

        OpenAIChatReqCommand openAIChatReqCommand = new OpenAIChatReqCommand();  //https://www.volcengine.com/docs/82379/1494384?redirect=1
        openAIChatReqCommand.setModel("doubao-1.5-pro-32k-250115"); // 选择真实的模型列表：https://www.volcengine.com/docs/82379/1330310#%E6%96%87%E6%9C%AC%E7%94%9F%E6%88%90
        openAIChatReqCommand.setStream(false);
        openAIChatReqCommand.setTemperature(0.7);

        // 添加消息列表
        List<OpenAIChatReqCommand.ChatMessage> chatMessageList = new ArrayList<>();
        OpenAIChatReqCommand.ChatMessage chatMessage = new OpenAIChatReqCommand.ChatMessage("user", "Hello, how are you?");
        chatMessageList.add(chatMessage);
        openAIChatReqCommand.setMessages(chatMessageList);

        // 调用同步阻塞接口
        OpenAIChatRespCommand openAIChatCompletions = aiSupplierChatClient.blockChatCompletion(openAIChatReqCommand);
        System.out.println(JsonUtils.toJsonString(openAIChatCompletions));
    }

}
