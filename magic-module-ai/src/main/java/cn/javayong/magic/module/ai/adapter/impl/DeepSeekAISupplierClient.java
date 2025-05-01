package cn.javayong.magic.module.ai.adapter.impl;

import cn.javayong.magic.module.ai.adapter.AISupplierClient;
import cn.javayong.magic.module.ai.adapter.AISupplierConfig;

/**
 * 标准 DeepSeek 供应商 API ，兼容 openai 协议
 */
public class DeepSeekAISupplierClient implements AISupplierClient {

    private AISupplierConfig aiSupplierConfig;

    @Override
    public void init(AISupplierConfig aiSupplierConfig) {
        this.aiSupplierConfig = aiSupplierConfig;
    }

    @Override
    public void destroy() {
    }

}
