package cn.javayong.magic.module.ai.mapper;

import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.framework.mybatis.core.mapper.BaseMapperX;
import cn.javayong.magic.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.javayong.magic.module.ai.domain.AiPlatformDO;
import cn.javayong.magic.module.ai.domain.vo.AiPlatformPageReqVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 平台配置 Mapper
 */
@Mapper
public interface AiPlatformMapper extends BaseMapperX<AiPlatformDO> {

    default PageResult<AiPlatformDO> selectPage(AiPlatformPageReqVO reqVO) {
        // 调用父类的 selectPage 方法执行分页查询
        return selectPage(
                // 第一个参数：分页请求对象（包含 pageNo/pageSize）
                reqVO,
                // 第二个参数：构建查询条件（使用 Lambda 表达式）
                new LambdaQueryWrapperX<AiPlatformDO>()
                        // 如果 reqVO.name 不为空，添加 LIKE 模糊查询条件（模型名称）
                        .likeIfPresent(AiPlatformDO::getName, reqVO.getName())
                        // 如果 reqVO.platform 不为空，添加 EQUAL 精确查询条件（平台类型）
                        .eqIfPresent(AiPlatformDO::getPlatform, reqVO.getPlatform())
                        // 添加排序规则（按 sort 字段升序）
                        .orderByAsc(AiPlatformDO::getSort)
        );
    }

}
