package cn.javayong.magic.module.ai.adapter.impl;

import cn.javayong.magic.framework.common.util.json.JsonUtils;
import cn.javayong.magic.module.ai.adapter.AISupplierChatClient;
import cn.javayong.magic.module.ai.adapter.AISupplierConfig;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatReqCommand;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

/**
 * 标准 DeepSeek 供应商 API ，兼容 openai 协议
 */
public class DeepSeekAISupplierChatClient implements AISupplierChatClient {

    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";

    final static String apiKey = "sk-31da87a7c6eb40188fb1a71f98fa6fbd";

    private AISupplierConfig aiSupplierConfig;

    @Override
    public void init(AISupplierConfig aiSupplierConfig) {
        this.aiSupplierConfig = aiSupplierConfig;
    }

    @Override
    public Flux<String> chatCompletion(OpenAIChatReqCommand openAIChatReqCommand) {
        // 1. 创建 WebClient (非Spring环境需手动构建)
        WebClient client = WebClient.builder()
                .baseUrl(API_URL)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();

        // 3. 发送流式请求并处理SSE响应
        Flux<String> sseStream = client.post()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_EVENT_STREAM) // 关键：声明接受SSE
                .bodyValue(JsonUtils.toJsonString(openAIChatReqCommand))
                .retrieve()
                .bodyToFlux(String.class);

        return sseStream;
    }

    @Override
    public void destroy() {

    }

}
