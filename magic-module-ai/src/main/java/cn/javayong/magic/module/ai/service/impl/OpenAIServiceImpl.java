package cn.javayong.magic.module.ai.service.impl;

import cn.javayong.magic.module.ai.adapter.AISupplierChatClient;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatReqCommand;
import cn.javayong.magic.module.ai.adapter.impl.DeepSeekAISupplierChatClient;
import cn.javayong.magic.module.ai.domain.convert.ChatConvert;
import cn.javayong.magic.module.ai.domain.vo.OpenAIChatReqVO;
import cn.javayong.magic.module.ai.service.OpenAIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class OpenAIServiceImpl implements OpenAIService {

    @Override
    public Object completions(OpenAIChatReqVO openAIChatReqVO) {
        OpenAIChatReqCommand openAIChatReqCommand = ChatConvert.INSTANCE.convert(openAIChatReqVO);
        AISupplierChatClient aiSupplierChatClient = new DeepSeekAISupplierChatClient();

        // 封装 SSE 流 ，并返回
        if (openAIChatReqVO.isStream()) {
            Flux<String> result = aiSupplierChatClient.streamChatCompletion(openAIChatReqCommand);
            return result.map(data -> ServerSentEvent.builder(data).build());
        }
        // 返回 JSON 实体
        else {
            String result = aiSupplierChatClient.blockChatCompletion(openAIChatReqCommand);
            return result;
        }
    }

}
