package cn.javayong.magic.module.ai.controller;

import cn.javayong.magic.framework.common.util.json.JsonUtils;
import cn.javayong.magic.module.ai.domain.AiOneApiTokenDO;
import cn.javayong.magic.module.ai.domain.vo.OpenAIChatReqVO;
import cn.javayong.magic.module.ai.service.AiOneApiTokenService;
import cn.javayong.magic.module.ai.service.OpenAIService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;

@Tag(name = "兼容 openai 的核心接口")
@RestController("OpenAIController")
@RequestMapping("/chat")
@Slf4j
public class OpenAIController {

    @Autowired
    private OpenAIService openAIService;

    @Resource
    private AiOneApiTokenService aiOneApiTokenService;

    /**
     * 创建文本对话请求 参考 deepseek 文档：  https://api-docs.deepseek.com/zh-cn/
     * 或者 硅基流动  https://docs.siliconflow.cn/cn/api-reference/chat-completions/chat-completions
     */
    @RequestMapping(value = "/completions",
            produces = {MediaType.TEXT_EVENT_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @PermitAll
    public Object completions(@RequestBody OpenAIChatReqVO openAIChatReqVO,
                              @RequestHeader(value = "Authorization", required = false) String oneApiToken ,
                              HttpServletResponse response) {
        log.info("openAIChatReqVO:" + JsonUtils.toJsonString(openAIChatReqVO) + " oneApiToken:" + oneApiToken);
        AiOneApiTokenDO aiOneApiTokenDO = aiOneApiTokenService.getOneApiTokenByToken(oneApiToken);

        // 流式或者非流式 设置不同的 ContentType
        if(openAIChatReqVO.isStream()) {
            response.setContentType(MediaType.TEXT_EVENT_STREAM.getType());
        } else {
            response.setContentType(MediaType.APPLICATION_JSON.getType());
        }
        return openAIService.completions(openAIChatReqVO);
    }

}