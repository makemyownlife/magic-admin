package cn.javayong.magic.module.ai.adapter.impl;

import cn.javayong.magic.module.ai.adapter.AISupplierClient;
import cn.javayong.magic.module.ai.adapter.AISupplierConfig;

/**
 * 阿里千问 AI 供应商 API 实现
 */
public class QwenAISupplierClient implements AISupplierClient {

    private AISupplierConfig aiSupplierConfig;

    @Override
    public void init(AISupplierConfig aiSupplierConfig) {
        this.aiSupplierConfig = aiSupplierConfig;
    }

    @Override
    public void destroy() {

    }

}
