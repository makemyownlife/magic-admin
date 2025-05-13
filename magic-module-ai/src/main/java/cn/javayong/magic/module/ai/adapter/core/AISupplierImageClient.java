package cn.javayong.magic.module.ai.adapter.core;

import cn.javayong.magic.module.ai.adapter.command.OpenAIChatCompletions;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatReqCommand;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatRespCommand;
import reactor.core.publisher.Flux;

/**
 * 大模型 API 供应商图片生成客户端
 */
public interface AISupplierImageClient {

    void init(AISupplierConfig aiSupplierConfig);

}
