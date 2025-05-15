package cn.javayong.magic.module.ai.service.impl;

import cn.javayong.magic.framework.common.exception.util.ServiceExceptionUtil;
import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.framework.common.util.json.JsonUtils;
import cn.javayong.magic.framework.common.util.object.BeanUtils;
import cn.javayong.magic.module.ai.domain.AiPlatformDO;
import cn.javayong.magic.module.ai.domain.enums.AiPlatformEnum;
import cn.javayong.magic.module.ai.domain.vo.AiPlatformPageReqVO;
import cn.javayong.magic.module.ai.domain.vo.AiPlatformSaveReqVO;
import cn.javayong.magic.module.ai.mapper.AiPlatformMapper;
import cn.javayong.magic.module.ai.service.AiPlatformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import static cn.javayong.magic.module.ai.domain.enums.ErrorCodeConstants.PLATFORM_NOT_EXISTS;

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

    @Override
    @Transactional
    public Long createPlatform(AiPlatformSaveReqVO createReqVO) {
        // 1. 校验
        AiPlatformEnum.validatePlatform(createReqVO.getPlatform());

        // 2. 插入 ai_platform
        AiPlatformDO platformDO = BeanUtils.toBean(createReqVO, AiPlatformDO.class);
        platformDO.setDeleted(false);
        platformDO.setModelIds(JsonUtils.toJsonString(createReqVO.getModelIds()));
        aiPlatformMapper.insert(platformDO);

        // 3. 插入到 ai_platform_model_mapping

        return platformDO.getId();
    }

    @Override
    public void updatePlatform(AiPlatformSaveReqVO saveReqVO) {
        // 1. 校验
        validatePlatformExists(saveReqVO.getId());
        AiPlatformEnum.validatePlatform(saveReqVO.getPlatform());

        // 2. 更新
        AiPlatformDO updateObj = BeanUtils.toBean(saveReqVO, AiPlatformDO.class);
        updateObj.setModelIds(JsonUtils.toJsonString(saveReqVO.getModelIds()));

        aiPlatformMapper.updateById(updateObj);
    }

    private AiPlatformDO validatePlatformExists(Long id) {
        AiPlatformDO platformDO = aiPlatformMapper.selectById(id);
        if (platformDO  == null) {
            throw ServiceExceptionUtil.exception(PLATFORM_NOT_EXISTS);
        }
        return platformDO;
    }

}
