package cn.javayong.magic.module.ai.adapter.impl;

import cn.javayong.magic.module.ai.adapter.AISupplierClient;
import cn.javayong.magic.module.ai.adapter.AISupplierConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

/**
 * 标准 DeepSeek 供应商 API ，兼容 openai 协议
 */
public class DeepSeekAISupplierClient implements AISupplierClient {

    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";

    final static String apiKey = "sk-31da87a7c6eb40188fb1a71f98fa6fbd";

    private AISupplierConfig aiSupplierConfig;

    @Override
    public void init(AISupplierConfig aiSupplierConfig) {
        this.aiSupplierConfig = aiSupplierConfig;
    }

    @Override
    public Flux<String> chatCompletion() {
        // 1. 创建 WebClient (非Spring环境需手动构建)
        WebClient client = WebClient.builder()
                .baseUrl(API_URL)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();

        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", "我喜欢编程 怎么办");

        // 3. 构建消息数组
        JsonArray messages = new JsonArray();
        messages.add(userMessage);
        // 2. 构建请求体 (与你的原始代码一致)
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "deepseek-chat");
        requestBody.add("messages", messages); // 添加消息内容
        requestBody.addProperty("temperature", 0.7);
        requestBody.addProperty("max_tokens", 4048);
        requestBody.addProperty("stream", true);

        // 3. 发送流式请求并处理SSE响应
        Flux<String> sseStream = client.post()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_EVENT_STREAM) // 关键：声明接受SSE
                .bodyValue(requestBody.toString())
                .retrieve()
                .bodyToFlux(String.class)
                .doOnNext(line -> System.out.println("RAW SSE LINE: " + line));  // 打印原始数据

        return sseStream;
    }

    @Override
    public void destroy() {

    }

}
