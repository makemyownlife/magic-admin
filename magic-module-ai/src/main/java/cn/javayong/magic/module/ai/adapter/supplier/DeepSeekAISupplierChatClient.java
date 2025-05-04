package cn.javayong.magic.module.ai.adapter.supplier;

import cn.javayong.magic.framework.common.util.json.JsonUtils;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatReqCommand;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatCompletions;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatRespCommand;
import cn.javayong.magic.module.ai.adapter.core.AISupplierChatClient;
import cn.javayong.magic.module.ai.adapter.core.AISupplierConfig;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * 标准 DeepSeek 供应商 API ，兼容 openai 协议
 */
@Slf4j
public class DeepSeekAISupplierChatClient implements AISupplierChatClient {

    // 1. 配置 API 基础路径和端点（更灵活）
    private static final String BASE_URL = "https://api.deepseek.com/v1/";

    private static final String CHAT_COMPLETIONS_ENDPOINT = "/chat/completions";

    final static String apiKey = "sk-31da87a7c6eb40188fb1a71f98fa6fbd";

    private AISupplierConfig aiSupplierConfig;

    @Override
    public void init(AISupplierConfig aiSupplierConfig) {
        this.aiSupplierConfig = aiSupplierConfig;
    }

    @Override
    public Flux<String> streamChatCompletion(OpenAIChatReqCommand openAIChatReqCommand) {
        // 1. 创建 WebClient (非 Spring 环境需手动构建)
        WebClient client = WebClient.builder()
                .baseUrl(aiSupplierConfig.getBaseUrl())
                .defaultHeader("Authorization", "Bearer " + aiSupplierConfig.getApiKey())
                .build();

        // 2. 发送流式请求并处理 SSE 响应
        Flux<String> sseStream = client.post()
                .uri(CHAT_COMPLETIONS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_EVENT_STREAM) // 关键：声明接受SSE
                .bodyValue(JsonUtils.toJsonString(openAIChatReqCommand))
                .retrieve()
                .bodyToFlux(String.class);
        //  .doOnNext(line -> System.out.println("RAW SSE LINE: " + line));  // 打印原始数据

        return sseStream;
    }

    @Override
    public OpenAIChatRespCommand blockChatCompletion(OpenAIChatReqCommand openAIChatReqCommand) {
        OpenAIChatRespCommand respCommand = new OpenAIChatRespCommand();
        try {
            // 1. 创建 WebClient (非Spring环境需手动构建)
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
            respCommand.setCode(OpenAIChatRespCommand.INTERNEL_ERROR_CODE);
            respCommand.setMessage("调用 deepseek 官网API服务异常，请确保 API KEY 正确 和网络正常!");
        }
        return respCommand;
    }

    @Override
    public void destroy() {
    }

    public static void main(String[] args) {
        AISupplierConfig aiSupplierConfig = new AISupplierConfig();
        aiSupplierConfig.setBaseUrl("https://api.deepseek.com/v1/");
        aiSupplierConfig.setApiKey("sk-31da87a7c6eb40188fb1a71f98fa6fbd");

        AISupplierChatClient aiSupplierChatClient = new DeepSeekAISupplierChatClient();
        aiSupplierChatClient.init(aiSupplierConfig);

        OpenAIChatReqCommand openAIChatReqCommand = new OpenAIChatReqCommand();
        openAIChatReqCommand.setModel("deepseek-chat");
        openAIChatReqCommand.setStream(false);
        openAIChatReqCommand.setTemperature(0.7);

        // 添加消息列表
        List<OpenAIChatReqCommand.ChatMessage> chatMessageList = new ArrayList<>();
        OpenAIChatReqCommand.ChatMessage chatMessage = new OpenAIChatReqCommand.ChatMessage("user", "Hello, how are you?");
        chatMessageList.add(chatMessage);
        openAIChatReqCommand.setMessages(chatMessageList);

        OpenAIChatRespCommand openAIChatCompletions = aiSupplierChatClient.blockChatCompletion(openAIChatReqCommand);
        System.out.println(JSON.toJSONString(openAIChatCompletions));

        aiSupplierChatClient.destroy();
    }

}
