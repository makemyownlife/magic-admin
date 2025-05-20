package cn.javayong.magic.module.ai.service;

import cn.javayong.magic.module.ai.adapter.command.OpenAIChatCompletions;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatRespCommand;
import cn.javayong.magic.module.ai.domain.vo.OpenAIChatReqVO;
import reactor.core.publisher.Flux;

public interface OpenAIService {

    OpenAIChatRespCommand<Flux<String>> streamCompletions(OpenAIChatReqVO openAIChatReqVO);

    OpenAIChatRespCommand<OpenAIChatCompletions> blockCompletions(OpenAIChatReqVO openAIChatReqVO);

}
