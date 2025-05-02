package cn.javayong.magic.module.ai.adapter;

import cn.javayong.magic.module.ai.adapter.command.OpenAIChatReqCommand;
import reactor.core.publisher.Flux;

/**
 * 供应商对话客户端
 */
public interface AISupplierChatClient {

    void init(AISupplierConfig aiSupplierConfig);

    Flux<String> chatCompletion(OpenAIChatReqCommand openAIChatReqCommand);

    void destroy();

}
