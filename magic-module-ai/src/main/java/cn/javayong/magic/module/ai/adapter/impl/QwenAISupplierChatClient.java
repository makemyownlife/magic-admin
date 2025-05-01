package cn.javayong.magic.module.ai.adapter.impl;

import cn.javayong.magic.module.ai.adapter.AISupplierChatClient;
import cn.javayong.magic.module.ai.adapter.AISupplierConfig;
import reactor.core.publisher.Flux;

/**
 * 阿里千问 AI 供应商 API 实现
 */
public class QwenAISupplierChatClient implements AISupplierChatClient {

    private AISupplierConfig aiSupplierConfig;

    @Override
    public void init(AISupplierConfig aiSupplierConfig) {
        this.aiSupplierConfig = aiSupplierConfig;
    }

    @Override
    public Flux<String> chatCompletion() {
        return null;
    }

    @Override
    public void destroy() {

    }

}
