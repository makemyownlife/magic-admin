package cn.javayong.magic.module.ai.service.impl;

import cn.javayong.magic.framework.common.util.json.JsonUtils;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatReqCommand;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatCompletions;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatRespCommand;
import cn.javayong.magic.module.ai.adapter.core.AISupplierChatClient;
import cn.javayong.magic.module.ai.adapter.core.AISupplierConfig;
import cn.javayong.magic.module.ai.adapter.supplier.DeepSeekAISupplierChatClient;
import cn.javayong.magic.module.ai.adapter.supplier.DouBaoAISupplierChatClient;
import cn.javayong.magic.module.ai.adapter.supplier.QwenAISupplierChatClient;
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

        AISupplierConfig aiSupplierConfig = new AISupplierConfig();
        aiSupplierConfig.setBaseUrl("https://ark.cn-beijing.volces.com/api/v3/");
        aiSupplierConfig.setApiKey("11515f06-c8fe-4532-83b8-7d5145bd3132");

        AISupplierChatClient aiSupplierChatClient = new DouBaoAISupplierChatClient();
        aiSupplierChatClient.init(aiSupplierConfig);

        // 封装 SSE 流
        if (openAIChatReqVO.isStream()) {
            OpenAIChatRespCommand<Flux<String>> respCommand = aiSupplierChatClient.streamChatCompletion(openAIChatReqCommand);
            if (respCommand.getCode() != OpenAIChatRespCommand.SUCCESS_CODE){
                return JsonUtils.toJsonString(respCommand);
            }
            return respCommand.getData().map(data -> ServerSentEvent.builder(data).build());
        }

        // 返回 JSON 实体
        else {
            OpenAIChatRespCommand<OpenAIChatCompletions> respCommand = aiSupplierChatClient.blockChatCompletion(openAIChatReqCommand);
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
