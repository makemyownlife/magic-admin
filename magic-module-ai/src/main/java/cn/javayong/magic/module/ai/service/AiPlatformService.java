package cn.javayong.magic.module.ai.service;

import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.module.ai.domain.AiModelDO;
import cn.javayong.magic.module.ai.domain.AiPlatformDO;
import cn.javayong.magic.module.ai.domain.vo.AiModelPageReqVO;
import cn.javayong.magic.module.ai.domain.vo.AiModelSaveReqVO;
import cn.javayong.magic.module.ai.domain.vo.AiPlatformPageReqVO;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.util.List;

/**
 * AI 平台配置（baseUrl 、模型绑定） Service 接口
 */
public interface AiPlatformService {

    PageResult<AiPlatformDO> getModelPage(AiPlatformPageReqVO pageReqVO);

    AiPlatformDO getPlatform(Long id);

}
