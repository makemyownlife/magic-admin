package cn.javayong.magic.module.ai.adapter.core;

import cn.javayong.magic.module.ai.adapter.command.OpenAIChatReqCommand;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatCompletions;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatRespCommand;
import reactor.core.publisher.Flux;

/**
 * 大模型 API 供应商对话客户端
 */
public interface AiPlatformChatClient {

    void init(AiPlatformConfig aiSupplierConfig);

    OpenAIChatRespCommand<Flux<String>> streamChatCompletion(OpenAIChatReqCommand openAIChatReqCommand);

    OpenAIChatRespCommand<OpenAIChatCompletions> blockChatCompletion(OpenAIChatReqCommand openAIChatReqCommand);

}
