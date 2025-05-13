package cn.javayong.magic.module.ai.service.impl;

import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.module.ai.domain.AiPlatformDO;
import cn.javayong.magic.module.ai.domain.vo.AiPlatformPageReqVO;
import cn.javayong.magic.module.ai.mapper.AiPlatformMapper;
import cn.javayong.magic.module.ai.service.AiPlatformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * AI 平台配置( baseUrl 以及 ) Service 接口
 */
@Service
@Validated
@Slf4j
public class AiPlatformServiceImpl implements AiPlatformService {

    @Resource
    private AiPlatformMapper aiPlatformMapper;

    @Override
    public PageResult<AiPlatformDO> getModelPage(AiPlatformPageReqVO pageReqVO) {
        return aiPlatformMapper.selectPage(pageReqVO);
    }

    @Override
    public AiPlatformDO getPlatform(Long id) {
        return aiPlatformMapper.selectById(id);
    }

}
