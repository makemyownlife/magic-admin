package cn.javayong.magic.module.ai.service.impl;

import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.framework.common.util.json.JsonUtils;
import cn.javayong.magic.framework.common.util.object.BeanUtils;
import cn.javayong.magic.idgenerator.core.service.IdGeneratorService;
import cn.javayong.magic.module.ai.domain.AiOneApiTokenDO;
import cn.javayong.magic.module.ai.domain.vo.AiOneApiTokenPageReqVO;
import cn.javayong.magic.module.ai.domain.vo.AiOneApiTokenSaveReqVO;
import cn.javayong.magic.module.ai.mapper.AiOneApiTokenMapper;
import cn.javayong.magic.module.ai.service.AiOneApiTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * one API token 配置 实现类
 */
@Service
@Validated
@Slf4j
public class AiOneApiTokenServiceImpl implements AiOneApiTokenService {

    @Resource
    private AiOneApiTokenMapper oneApiTokenMapper;

    @Resource
    private IdGeneratorService idGeneratorService;

    @Override
    public PageResult<AiOneApiTokenDO> getOneApiTokenPage(AiOneApiTokenPageReqVO pageReqVO) {
        return oneApiTokenMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createOneApiToken(AiOneApiTokenSaveReqVO createReqVO) {

        AiOneApiTokenDO oneApiTokenDO = BeanUtils.toBean(createReqVO, AiOneApiTokenDO.class);

        // 通过 ID 生成器生成唯一 ID
        oneApiTokenDO.setId(idGeneratorService.createUniqueId("ai_oneapi_token"));

        // 通过 UUID 生成唯一 token
        oneApiTokenDO.setToken(UUID.randomUUID().toString());

        // 设置模型列表
        oneApiTokenDO.setModelIds(JsonUtils.toJsonString(createReqVO.getModelIds()));

        oneApiTokenDO.setDeleted(false);

        oneApiTokenMapper.insert(oneApiTokenDO);

        return oneApiTokenDO.getId();
    }

    @Override
    public void updateOneApiToken(AiOneApiTokenSaveReqVO saveReqVO) {
        AiOneApiTokenDO oneApiTokenDO = BeanUtils.toBean(saveReqVO, AiOneApiTokenDO.class);

        // 设置模型列表
        oneApiTokenDO.setModelIds(JsonUtils.toJsonString(saveReqVO.getModelIds()));

        oneApiTokenMapper.updateById(oneApiTokenDO);
    }

    @Override
    public void deleteOneApiToken(Long id) {
        oneApiTokenMapper.deleteById(id);
    }

    @Override
    public AiOneApiTokenDO getOneApiTokenByToken(String token) {
        return oneApiTokenMapper.selectOne(AiOneApiTokenDO::getToken, token);
    }

    @Override
    public AiOneApiTokenDO getOneApiTokenById(Long id) {
        return oneApiTokenMapper.selectById(id);
    }

}
