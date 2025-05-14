package cn.javayong.magic.module.ai.service.impl;

import cn.javayong.magic.framework.common.util.json.JsonUtils;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatReqCommand;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatCompletions;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatRespCommand;
import cn.javayong.magic.module.ai.adapter.core.AIPlatformChatClient;
import cn.javayong.magic.module.ai.adapter.core.AIPlatformConfig;
import cn.javayong.magic.module.ai.adapter.platform.DouBaoChatClient;
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

        AIPlatformConfig aiPlatformConfig = new AIPlatformConfig();
        aiPlatformConfig.setBaseUrl("https://api.deepseek.com/v1/");
        aiPlatformConfig.setApiKey("sk-31da87a7c6eb40188fb1a71f98fa6fbd");

        AIPlatformChatClient aiPlatformChatClient = new DouBaoChatClient();
        aiPlatformChatClient.init(aiPlatformConfig);

        // 封装 SSE 流
        if (openAIChatReqVO.isStream()) {
            OpenAIChatRespCommand<Flux<String>> respCommand = aiPlatformChatClient.streamChatCompletion(openAIChatReqCommand);
            if (respCommand.getCode() != OpenAIChatRespCommand.SUCCESS_CODE){
                return JsonUtils.toJsonString(respCommand);
            }
            return respCommand.getData().map(data -> ServerSentEvent.builder(data).build());
        }

        // 返回 JSON 实体
        else {
            OpenAIChatRespCommand<OpenAIChatCompletions> respCommand = aiPlatformChatClient.blockChatCompletion(openAIChatReqCommand);
            if (respCommand.getData() != null) {
                return JsonUtils.toJsonString(respCommand.getData());
            } else {
                // 异常格式：{
                //  "code": 20012,
                //  "message": "<string>",
                //  "data": "<string>"
                //}
                return JsonUtils.toJsonString(respCommand);
            }
        }

    }

}
