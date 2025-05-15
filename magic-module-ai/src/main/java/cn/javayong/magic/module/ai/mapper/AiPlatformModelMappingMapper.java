package cn.javayong.magic.module.ai.mapper;

import cn.javayong.magic.framework.mybatis.core.mapper.BaseMapperX;
import cn.javayong.magic.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.javayong.magic.module.ai.domain.AiModelDO;
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

    default List<AiPlatformModelMappingDO> getModelMappingListByModelName(String modelName) {
        return selectList(new LambdaQueryWrapperX<AiPlatformModelMappingDO>()
                // 查询未删除的记录
                .eq(AiPlatformModelMappingDO::getDeleted, false)
                // 匹配 model 字段或 mappingName 字段
                .and(wrapper -> wrapper
                        .eq(AiPlatformModelMappingDO::getModel, modelName)
                        .or()
                        .eq(AiPlatformModelMappingDO::getMappingName, modelName)
                )
                // 可以添加排序
                .orderByAsc(AiPlatformModelMappingDO::getId)
        );
    }

}
