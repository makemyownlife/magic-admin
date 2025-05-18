package cn.javayong.magic.module.ai.service;

import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.module.ai.domain.AiOneApiTokenDO;
import cn.javayong.magic.module.ai.domain.vo.AiModelPageReqVO;

/**
 * one API token 配置
 */
public interface AiOneApiTokenService {

    PageResult<AiOneApiTokenDO> getOneApiTokenPage(AiModelPageReqVO pageReqVO);

}
