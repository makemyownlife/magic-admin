package cn.javayong.magic.module.ai.service;

import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.module.ai.domain.AiOneApiTokenDO;
import cn.javayong.magic.module.ai.domain.vo.AiOneApiTokenPageReqVO;
import cn.javayong.magic.module.ai.domain.vo.AiOneApiTokenSaveReqVO;

import javax.validation.Valid;

/**
 * one API token 配置
 */
public interface AiOneApiTokenService {

    PageResult<AiOneApiTokenDO> getOneApiTokenPage(AiOneApiTokenPageReqVO pageReqVO);

    Long createOneApiToken(@Valid AiOneApiTokenSaveReqVO createReqVO);

    void deleteOneApiToken(Long id);

}
