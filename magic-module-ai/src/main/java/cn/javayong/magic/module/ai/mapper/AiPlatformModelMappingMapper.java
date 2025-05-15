package cn.javayong.magic.module.ai.mapper;

import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.framework.mybatis.core.mapper.BaseMapperX;
import cn.javayong.magic.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.javayong.magic.module.ai.domain.AiPlatformDO;
import cn.javayong.magic.module.ai.domain.vo.AiPlatformPageReqVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 平台配置 模型名映射 Mapper
 */
@Mapper
public interface AiPlatformModelMappingMapper extends BaseMapperX<AiPlatformDO> {



}
