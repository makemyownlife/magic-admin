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

import java.util.ArrayList;
import java.util.List;

/**
 * 阿里千问 AI 供应商 API 实现
 */
@Slf4j
public class QwenChatClient implements AiPlatformChatClient {

    private static final String CHAT_COMPLETIONS_ENDPOINT = "/chat/completions";

    private AiPlatformConfig aiPlatformConfig;

    public QwenChatClient(AiPlatformConfig aiPlatformConfig) {
        this.aiPlatformConfig = aiPlatformConfig;
    }

    @Override
    public OpenAIChatRespCommand<Flux<String>> streamChatCompletion(OpenAIChatReqCommand openAIChatReqCommand) {
        OpenAIChatRespCommand respCommand = new OpenAIChatRespCommand();
        try {
            // 1. 创建 WebClient
            WebClient client = WebClient.builder()
                    .baseUrl(aiPlatformConfig.getBaseUrl())
                    .defaultHeader("Authorization", "Bearer " + aiPlatformConfig.getApiKey())
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
                                                    "invoke Qwen API Error: HTTP " + response.statusCode() + " - " + errorBody
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

            respCommand.setCode(OpenAIChatRespCommand.SUCCESS_CODE);
            respCommand.setData(stringFlux);
            return respCommand;
        } catch (Exception e) {
            log.error("Qwen invoke api error:", e);
            respCommand.setCode(OpenAIChatRespCommand.INTERNAL_ERROR_CODE);
            respCommand.setData(e.getMessage());
            return respCommand;
        }
    }

    @Override
    public OpenAIChatRespCommand<OpenAIChatCompletions> blockChatCompletion(OpenAIChatReqCommand openAIChatReqCommand) {
        OpenAIChatRespCommand respCommand = new OpenAIChatRespCommand();
        try {
            // 1. 创建 WebClient (非Spring环境需手动构建)
            WebClient client = WebClient.builder()
                    .baseUrl(aiPlatformConfig.getBaseUrl())
                    .defaultHeader("Authorization", "Bearer " + aiPlatformConfig.getApiKey())
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
            log.error("QianWen blockChatCompletion invoke error:", e);
            respCommand.setCode(OpenAIChatRespCommand.INTERNAL_ERROR_CODE);
            respCommand.setMessage("调用 阿里云千问官网API服务异常，请确保 API KEY 正确 和网络正常!");
        }
        return respCommand;
    }

    public static void main(String[] args) throws InterruptedException {

        AiPlatformConfig aiSupplierConfig = new AiPlatformConfig();
        aiSupplierConfig.setBaseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1/");
        aiSupplierConfig.setApiKey("sk-f49ab9cd447e433c8862dc9f66cf432a");

        AiPlatformChatClient aiPlatformChatClient = new QwenChatClient(aiSupplierConfig);

        OpenAIChatReqCommand openAIChatReqCommand = new OpenAIChatReqCommand();
        openAIChatReqCommand.setModel("qwen-turbo"); // 模型列表：https://www.alibabacloud.com/help/zh/model-studio/models
        openAIChatReqCommand.setStream(false);
        openAIChatReqCommand.setTemperature(0.7);

        // 添加消息列表
        List<OpenAIChatReqCommand.ChatMessage> chatMessageList = new ArrayList<>();
        OpenAIChatReqCommand.ChatMessage chatMessage = new OpenAIChatReqCommand.ChatMessage("user", "Hello, how are you?");
        chatMessageList.add(chatMessage);
        openAIChatReqCommand.setMessages(chatMessageList);

        // 调用同步阻塞接口
        OpenAIChatRespCommand openAIChatCompletions = aiPlatformChatClient.blockChatCompletion(openAIChatReqCommand);
        System.out.println(JsonUtils.toJsonString(openAIChatCompletions));

        // Flux<String> openAIChatCompletions = aiSupplierChatClient.streamChatCompletion(openAIChatReqCommand);
    }

}
