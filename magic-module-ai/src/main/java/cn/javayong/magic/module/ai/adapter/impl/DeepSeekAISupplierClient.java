package cn.javayong.magic.module.ai.adapter.impl;

import cn.javayong.magic.module.ai.adapter.AISupplierClient;
import cn.javayong.magic.module.ai.adapter.AISupplierConfig;
import reactor.core.publisher.Flux;

/**
 * 标准 DeepSeek 供应商 API ，兼容 openai 协议
 */
public class DeepSeekAISupplierClient implements AISupplierClient {

    final static String apiUrl = "https://api.deepseek.com/v1/chat/completions";

    final static String apiKey = "sk-31da87a7c6eb40188fb1a71f98fa6fbd";

    private AISupplierConfig aiSupplierConfig;

    @Override
    public void init(AISupplierConfig aiSupplierConfig) {
        this.aiSupplierConfig = aiSupplierConfig;
    }


    @Override
    public void destroy() {

    }

}
