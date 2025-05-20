package cn.javayong.magic.module.ai.adapter.core;


import cn.javayong.magic.module.ai.adapter.platform.DeepSeekChatClient;
import cn.javayong.magic.module.ai.adapter.platform.DouBaoChatClient;
import cn.javayong.magic.module.ai.adapter.platform.QwenChatClient;
import cn.javayong.magic.module.ai.adapter.platform.SiliconFlowChatClient;
import cn.javayong.magic.module.ai.domain.enums.AiPlatformEnum;

/**
 * 大模型 API 供应商对话客户端
 */
public class AiPlatformClientFactory {

    public static AiPlatformChatClient createChatClient(AiPlatformConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be null");
        }

        switch (AiPlatformEnum.validatePlatform(config.getPlatform())) {
            case DEEP_SEEK:
                return new DeepSeekChatClient(config);
            case DOU_BAO:
                return new DouBaoChatClient(config);
            case TONG_YI:
                return new QwenChatClient(config);
            case SILICON_FLOW:
                return new SiliconFlowChatClient(config);
            default:
                throw new UnsupportedOperationException("Unsupported platform: " + config.getPlatform());
        }
        // 不需要最后的 return chatClient
    }

}
