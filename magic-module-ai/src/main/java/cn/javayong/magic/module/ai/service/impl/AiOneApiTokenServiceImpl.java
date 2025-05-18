package cn.javayong.magic.module.ai.service.impl;

import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.framework.common.util.object.BeanUtils;
import cn.javayong.magic.module.ai.domain.AiOneApiTokenDO;
import cn.javayong.magic.module.ai.domain.vo.AiOneApiTokenPageReqVO;
import cn.javayong.magic.module.ai.domain.vo.AiOneApiTokenSaveReqVO;
import cn.javayong.magic.module.ai.mapper.AiOneApiTokenMapper;
import cn.javayong.magic.module.ai.service.AiOneApiTokenService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * one API token 配置 实现类
 */
@Service
@Validated
public class AiOneApiTokenServiceImpl implements AiOneApiTokenService {

    @Resource
    private AiOneApiTokenMapper oneApiTokenMapper;

    @Override
    public PageResult<AiOneApiTokenDO> getOneApiTokenPage(AiOneApiTokenPageReqVO pageReqVO) {
        return oneApiTokenMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createOneApiToken(AiOneApiTokenSaveReqVO createReqVO) {
        AiOneApiTokenDO oneApiTokenDO = BeanUtils.toBean(createReqVO, AiOneApiTokenDO.class);
        oneApiTokenDO.setDeleted(false);
        oneApiTokenMapper.insert(oneApiTokenDO);
        return oneApiTokenDO.getId();
    }

}
