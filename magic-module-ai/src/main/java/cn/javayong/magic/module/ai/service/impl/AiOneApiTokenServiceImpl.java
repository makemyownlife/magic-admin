package cn.javayong.magic.module.ai.service.impl;

import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.module.ai.domain.AiOneApiTokenDO;
import cn.javayong.magic.module.ai.domain.vo.AiModelPageReqVO;
import cn.javayong.magic.module.ai.service.AiOneApiTokenService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * one API token 配置 实现类
 */
@Service
@Validated
public class AiOneApiTokenServiceImpl implements AiOneApiTokenService {

    @Override
    public PageResult<AiOneApiTokenDO> getOneApiTokenPage(AiModelPageReqVO pageReqVO) {
        return null;
    }

}
