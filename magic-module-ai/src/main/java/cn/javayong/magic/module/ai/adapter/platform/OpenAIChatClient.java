package cn.javayong.magic.module.ai.adapter.platform;

import cn.javayong.magic.module.ai.adapter.command.OpenAIChatCompletions;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatReqCommand;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatRespCommand;
import cn.javayong.magic.module.ai.adapter.core.AiPlatformChatClient;
import reactor.core.publisher.Flux;

public class OpenAIChatClient implements AiPlatformChatClient {

    @Override
    public OpenAIChatRespCommand<Flux<String>> streamChatCompletion(OpenAIChatReqCommand openAIChatReqCommand) {
        return null;
    }

    @Override
    public OpenAIChatRespCommand<OpenAIChatCompletions> blockChatCompletion(OpenAIChatReqCommand openAIChatReqCommand) {
        return null;
    }

}
