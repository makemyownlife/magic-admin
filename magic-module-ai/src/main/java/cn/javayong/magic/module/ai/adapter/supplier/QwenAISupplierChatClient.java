package cn.javayong.magic.module.ai.adapter.supplier;

import cn.javayong.magic.module.ai.adapter.command.OpenAIChatReqCommand;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatCompletions;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatRespCommand;
import cn.javayong.magic.module.ai.adapter.core.AISupplierChatClient;
import cn.javayong.magic.module.ai.adapter.core.AISupplierConfig;
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
    public Flux<String> streamChatCompletion(OpenAIChatReqCommand openAIChatReqCommand) {
        return null;
    }

    @Override
    public OpenAIChatRespCommand<OpenAIChatCompletions> blockChatCompletion(OpenAIChatReqCommand openAIChatReqCommand) {
        return null;
    }

    @Override
    public void destroy() {

    }

}
