package cn.javayong.magic.module.ai.mapper;

import cn.javayong.magic.framework.mybatis.core.mapper.BaseMapperX;
import cn.javayong.magic.module.ai.domain.AiPlatformModelMappingDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 平台配置 模型名映射 Mapper
 */
@Mapper
public interface AiPlatformModelMappingMapper extends BaseMapperX<AiPlatformModelMappingDO> {

    default List<AiPlatformModelMappingDO> getModelMappingList(Long platformId) {
        return selectList(AiPlatformModelMappingDO::getPlatformId , platformId);
    }

}
