package cn.javayong.magic.module.ai.service.impl;

import cn.javayong.magic.framework.common.util.json.JsonUtils;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatReqCommand;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatCompletions;
import cn.javayong.magic.module.ai.adapter.command.OpenAIChatRespCommand;
import cn.javayong.magic.module.ai.adapter.core.AiPlatformChatClient;
import cn.javayong.magic.module.ai.adapter.core.AiPlatformClientFactory;
import cn.javayong.magic.module.ai.adapter.core.AiPlatformConfig;
import cn.javayong.magic.module.ai.domain.AiOneApiTokenDO;
import cn.javayong.magic.module.ai.domain.AiPlatformDO;
import cn.javayong.magic.module.ai.domain.AiPlatformModelMappingDO;
import cn.javayong.magic.module.ai.domain.convert.ChatConvert;
import cn.javayong.magic.module.ai.domain.vo.OpenAIChatReqVO;
import cn.javayong.magic.module.ai.mapper.AiPlatformMapper;
import cn.javayong.magic.module.ai.mapper.AiPlatformModelMappingMapper;
import cn.javayong.magic.module.ai.service.AiOneApiTokenService;
import cn.javayong.magic.module.ai.service.OpenAIService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OpenAIServiceImpl implements OpenAIService {

    @Resource
    private AiPlatformModelMappingMapper aiPlatformModelMappingMapper;

    @Resource
    private AiPlatformMapper aiPlatformMapper;

    @Resource
    private AiOneApiTokenService aiOneApiTokenService;

    @Override
    public OpenAIChatRespCommand<Flux<String>> streamCompletions(OpenAIChatReqVO openAIChatReqVO) {

        // 定义 response
        OpenAIChatRespCommand<Flux<String>> respCommand = new OpenAIChatRespCommand<Flux<String>>();

        // 定义请求 转换
        OpenAIChatReqCommand openAIChatReqCommand = ChatConvert.INSTANCE.convert(openAIChatReqVO);

        // step1 : 根据 model 名称查询 平台和模型映射表
        List<AiPlatformModelMappingDO> platformModelMappingDOList = aiPlatformModelMappingMapper.getModelMappingListByModelName(openAIChatReqCommand.getModel());

        // step2 : 平台配置列表
        List<Long> platformIds = platformModelMappingDOList.stream()
                .map(AiPlatformModelMappingDO::getPlatformId)
                .distinct()
                .collect(Collectors.toList());
        List<AiPlatformDO> aiPlatformDOLIst = aiPlatformMapper.selectAvailablePlatformList(platformIds);
        if (CollectionUtils.isEmpty(aiPlatformDOLIst)) {
            log.error("幻视后台没有配置模型:" + openAIChatReqCommand.getModel() + " 支持的平台");
            respCommand.setCode(OpenAIChatRespCommand.INTERNAL_ERROR_CODE);
            respCommand.setMessage("幻视后台没有配置该模型的平台");
            return respCommand;
        }

        // step3: 随机选择一个平台配置
        Collections.shuffle(aiPlatformDOLIst);
        AiPlatformDO aiPlatformDO = aiPlatformDOLIst.get(0);

        // step4 : 创建对话客户端配置
        AiPlatformConfig aiPlatformConfig = new AiPlatformConfig();
        aiPlatformConfig.setBaseUrl(aiPlatformDO.getBaseUrl());
        aiPlatformConfig.setApiKey(aiPlatformDO.getApiKey());
        aiPlatformConfig.setPlatform(aiPlatformDO.getPlatform());

        //  step5  : 工厂模式创建对话客户端
        AiPlatformChatClient aiPlatformChatClient = AiPlatformClientFactory.createChatClient(aiPlatformConfig);

        // step6 : 重定向 模型名称（因为标准模型名 在不同平台的名称可能不相同）
        String targetModelName = platformModelMappingDOList.stream()
                .filter(mapping -> mapping.getPlatformId().equals(aiPlatformDO.getId()))
                .filter(mapping -> mapping.getModel().equals(openAIChatReqCommand.getModel()))
                .findFirst()
                .map(AiPlatformModelMappingDO::getMappingName)
                .orElse(openAIChatReqCommand.getModel()); // 如果找不到映射，使用原始模型名
        openAIChatReqCommand.setModel(targetModelName);

        log.info("本次聊天平台是：" + aiPlatformDO.getPlatform() + " 请求地址：" + aiPlatformDO.getBaseUrl() + " stream:" + openAIChatReqCommand.isStream() + " 目标模型：" + targetModelName);

        respCommand = aiPlatformChatClient.streamChatCompletion(openAIChatReqCommand);
        return respCommand;
    }

    public OpenAIChatRespCommand<OpenAIChatCompletions> blockCompletions(OpenAIChatReqVO openAIChatReqVO) {

        // 定义 response
        OpenAIChatRespCommand<OpenAIChatCompletions> respCommand = new OpenAIChatRespCommand<OpenAIChatCompletions>();

        // 定义请求 转换
        OpenAIChatReqCommand openAIChatReqCommand = ChatConvert.INSTANCE.convert(openAIChatReqVO);

        // step1 : 根据 model 名称查询 平台和模型映射表
        List<AiPlatformModelMappingDO> platformModelMappingDOList = aiPlatformModelMappingMapper.getModelMappingListByModelName(openAIChatReqCommand.getModel());

        // step2 : 平台配置列表
        List<Long> platformIds = platformModelMappingDOList.stream()
                .map(AiPlatformModelMappingDO::getPlatformId)
                .distinct()
                .collect(Collectors.toList());
        List<AiPlatformDO> aiPlatformDOLIst = aiPlatformMapper.selectAvailablePlatformList(platformIds);
        if (CollectionUtils.isEmpty(aiPlatformDOLIst)) {
            log.error("幻视后台没有配置模型:" + openAIChatReqCommand.getModel() + " 支持的平台");
            respCommand.setCode(OpenAIChatRespCommand.INTERNAL_ERROR_CODE);
            respCommand.setMessage("幻视后台没有配置该模型的平台");
            return respCommand;
        }

        // step3: 随机选择一个平台配置
        Collections.shuffle(aiPlatformDOLIst);
        AiPlatformDO aiPlatformDO = aiPlatformDOLIst.get(0);

        // step4 : 创建对话客户端配置
        AiPlatformConfig aiPlatformConfig = new AiPlatformConfig();
        aiPlatformConfig.setBaseUrl(aiPlatformDO.getBaseUrl());
        aiPlatformConfig.setApiKey(aiPlatformDO.getApiKey());
        aiPlatformConfig.setPlatform(aiPlatformDO.getPlatform());

        //  step5  : 工厂模式创建对话客户端
        AiPlatformChatClient aiPlatformChatClient = AiPlatformClientFactory.createChatClient(aiPlatformConfig);

        // step6 : 重定向 模型名称（因为标准模型名 在不同平台的名称可能不相同）
        String targetModelName = platformModelMappingDOList.stream()
                .filter(mapping -> mapping.getPlatformId().equals(aiPlatformDO.getId()))
                .filter(mapping -> mapping.getModel().equals(openAIChatReqCommand.getModel()))
                .findFirst()
                .map(AiPlatformModelMappingDO::getMappingName)
                .orElse(openAIChatReqCommand.getModel()); // 如果找不到映射，使用原始模型名
        openAIChatReqCommand.setModel(targetModelName);

        log.info("本次聊天平台是：" + aiPlatformDO.getPlatform() + " 请求地址：" + aiPlatformDO.getBaseUrl() + " stream:" + openAIChatReqCommand.isStream() + " 目标模型：" + targetModelName);

        respCommand = aiPlatformChatClient.blockChatCompletion(openAIChatReqCommand);
        return respCommand;
    }

}
