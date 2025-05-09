package cn.javayong.magic.module.ai.adapter.supplier;

import cn.javayong.magic.framework.common.util.json.JsonUtils;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatReqCommand;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatCompletions;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatRespCommand;
import cn.javayong.magic.module.ai.adapter.core.AISupplierChatClient;
import cn.javayong.magic.module.ai.adapter.core.AISupplierConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * 标准 DeepSeek 供应商 API ，兼容 openai 协议
 */
@Slf4j
public class DeepSeekAISupplierChatClient implements AISupplierChatClient {

    private static final String CHAT_COMPLETIONS_ENDPOINT = "/chat/completions";

    private AISupplierConfig aiSupplierConfig;

    @Override
    public void init(AISupplierConfig aiSupplierConfig) {
        this.aiSupplierConfig = aiSupplierConfig;
    }

    @Override
    public OpenAIChatRespCommand<Flux<String>> streamChatCompletion(OpenAIChatReqCommand openAIChatReqCommand) {
        OpenAIChatRespCommand respCommand = new OpenAIChatRespCommand();
        try {
            // 1. 创建 WebClient
            WebClient client = WebClient.builder()
                    .baseUrl(aiSupplierConfig.getBaseUrl())
                    .defaultHeader("Authorization", "Bearer " + aiSupplierConfig.getApiKey())
                    .build();

            // 2. 发送流式请求并处理 SSE 响应
            Flux<String> stringFlux = client.post()
                    .uri(CHAT_COMPLETIONS_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.TEXT_EVENT_STREAM) // 关键：声明接受 SSE
                    .bodyValue(JsonUtils.toJsonString(openAIChatReqCommand))
                    .retrieve()
                    // 检查 HTTP 状态码，如果不是 200，则返回错误信息
                    .onStatus(
                            status -> status != HttpStatus.OK,
                            response -> {
                                // 尝试读取错误信息（如 API 返回的错误 JSON）
                                return response.bodyToMono(String.class)
                                        .defaultIfEmpty("No error details provided")
                                        .flatMap(errorBody -> {
                                            log.error("API Error (HTTP {}): {}", response.statusCode(), errorBody);
                                            return Mono.error(new RuntimeException(
                                                    "invoke deepSeek API Error: HTTP " + response.statusCode() + " - " + errorBody
                                            ));
                                        });
                            }
                    )
                    .bodyToFlux(String.class)
                    // 错误处理（如网络异常）
                    .doOnError(error -> {
                        log.error("SSE Stream Error: ", error);
                    })
                    // 如果发生错误，返回一个错误提示消息
                    .onErrorResume(error -> {
                        return Flux.just("[ERROR] Stream failed: " + error.getMessage());
                    });
            System.out.println(1111);
            respCommand.setCode(OpenAIChatRespCommand.SUCCESS_CODE);
            respCommand.setData(stringFlux);
            return respCommand;
        } catch (Exception e) {
            log.error("deepseek invoke api error:", e);
            respCommand.setCode(OpenAIChatRespCommand.INTERNAL_ERROR_CODE);
            respCommand.setData(e.getMessage());
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
        AISupplierConfig aiSupplierConfig = new AISupplierConfig();
        aiSupplierConfig.setBaseUrl("https://api.deepseek.com/v1/");
        aiSupplierConfig.setApiKey("sk-31da87a7c6eb40188fb1a71f98fa6fbd");

        AISupplierChatClient aiSupplierChatClient = new DeepSeekAISupplierChatClient();
        aiSupplierChatClient.init(aiSupplierConfig);

        OpenAIChatReqCommand openAIChatReqCommand = new OpenAIChatReqCommand();
        openAIChatReqCommand.setModel("deepseek-chat");
        openAIChatReqCommand.setStream(true);
        openAIChatReqCommand.setTemperature(0.7);

        // 添加消息列表
        List<OpenAIChatReqCommand.ChatMessage> chatMessageList = new ArrayList<>();
        OpenAIChatReqCommand.ChatMessage chatMessage = new OpenAIChatReqCommand.ChatMessage("user", "Hello, how are you?");
        chatMessageList.add(chatMessage);
        openAIChatReqCommand.setMessages(chatMessageList);

        OpenAIChatRespCommand<Flux<String>> respCommand = aiSupplierChatClient.streamChatCompletion(openAIChatReqCommand);


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
