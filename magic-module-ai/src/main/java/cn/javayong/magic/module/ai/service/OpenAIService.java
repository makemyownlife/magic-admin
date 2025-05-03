package cn.javayong.magic.module.ai.service;

import cn.javayong.magic.module.ai.domain.vo.OpenAIChatReqVO;

public interface OpenAIService {

    Object completions(OpenAIChatReqVO openAIChatReqVO);

}
