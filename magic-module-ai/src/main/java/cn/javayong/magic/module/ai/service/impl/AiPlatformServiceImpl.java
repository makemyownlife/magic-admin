package cn.javayong.magic.module.ai.service.impl;

import cn.javayong.magic.framework.common.exception.util.ServiceExceptionUtil;
import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.framework.common.util.json.JsonUtils;
import cn.javayong.magic.framework.common.util.object.BeanUtils;
import cn.javayong.magic.module.ai.domain.AiPlatformDO;
import cn.javayong.magic.module.ai.domain.AiPlatformModelMappingDO;
import cn.javayong.magic.module.ai.domain.enums.AiPlatformEnum;
import cn.javayong.magic.module.ai.domain.vo.AiPlatformPageReqVO;
import cn.javayong.magic.module.ai.domain.vo.AiPlatformSaveReqVO;
import cn.javayong.magic.module.ai.mapper.AiPlatformMapper;
import cn.javayong.magic.module.ai.mapper.AiPlatformModelMappingMapper;
import cn.javayong.magic.module.ai.service.AiPlatformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import java.util.List;

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

    @Resource
    AiPlatformModelMappingMapper aiPlatformModelMappingMapper;

    @Override
    public PageResult<AiPlatformDO> getModelPage(AiPlatformPageReqVO pageReqVO) {
        return aiPlatformMapper.selectPage(pageReqVO);
    }

    @Override
    public AiPlatformDO getPlatform(Long id) {
        return aiPlatformMapper.selectById(id);
    }

    @Override
    public List<AiPlatformModelMappingDO> getModelMappingList(Long platformId) {
        return aiPlatformModelMappingMapper.getModelMappingList(platformId);
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
        List<AiPlatformSaveReqVO.ModelMapping> modelMappings = createReqVO.getModelMappings();
        for (AiPlatformSaveReqVO.ModelMapping modelMapping : modelMappings) {
            AiPlatformModelMappingDO aiPlatformModelMappingDO = new AiPlatformModelMappingDO();
            aiPlatformModelMappingDO.setPlatformId(platformDO.getId());
            aiPlatformModelMappingDO.setModelId(modelMapping.getId());
            aiPlatformModelMappingDO.setModel(modelMapping.getModel());
            aiPlatformModelMappingDO.setMappingName(modelMapping.getMappingName());
            aiPlatformModelMappingMapper.insert(aiPlatformModelMappingDO);
        }

        return platformDO.getId();
    }

    @Override
    @Transactional
    public void updatePlatform(AiPlatformSaveReqVO saveReqVO) {
        // 1. 校验
        validatePlatformExists(saveReqVO.getId());
        AiPlatformEnum.validatePlatform(saveReqVO.getPlatform());

        // 2. 更新
        AiPlatformDO updateObj = BeanUtils.toBean(saveReqVO, AiPlatformDO.class);
        updateObj.setModelIds(JsonUtils.toJsonString(saveReqVO.getModelIds()));
        aiPlatformMapper.updateById(updateObj);

        // 3. 映射表
        // 先删除表，然后新增
        aiPlatformModelMappingMapper.delete(AiPlatformModelMappingDO::getPlatformId, saveReqVO.getId());
        List<AiPlatformSaveReqVO.ModelMapping> modelMappings = saveReqVO.getModelMappings();
        for (AiPlatformSaveReqVO.ModelMapping modelMapping : modelMappings) {
            AiPlatformModelMappingDO aiPlatformModelMappingDO = new AiPlatformModelMappingDO();
            aiPlatformModelMappingDO.setPlatformId(saveReqVO.getId());
            aiPlatformModelMappingDO.setModelId(modelMapping.getId());
            aiPlatformModelMappingDO.setModel(modelMapping.getModel());
            aiPlatformModelMappingDO.setMappingName(modelMapping.getMappingName());
            aiPlatformModelMappingMapper.insert(aiPlatformModelMappingDO);
        }

    }

    private AiPlatformDO validatePlatformExists(Long id) {
        AiPlatformDO platformDO = aiPlatformMapper.selectById(id);
        if (platformDO == null) {
            throw ServiceExceptionUtil.exception(PLATFORM_NOT_EXISTS);
        }
        return platformDO;
    }

}
