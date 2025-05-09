package cn.javayong.magic.module.ai.mapper;

import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.framework.mybatis.core.mapper.BaseMapperX;
import cn.javayong.magic.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.javayong.magic.module.ai.domain.AiModelDO;
import cn.javayong.magic.module.ai.domain.vo.AiModelPageReqVO;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.Nullable;
import java.util.List;

/**
 * API 模型 Mapper
 */
@Mapper
public interface AiModelMapper extends BaseMapperX<AiModelDO> {

    /**
     * 分页查询 AI 模型数据
     * @param reqVO 分页查询请求参数（包含分页参数和过滤条件）
     * @return 分页结果对象（包含数据列表和分页信息）
     */
    default PageResult<AiModelDO> selectPage(AiModelPageReqVO reqVO) {
        // 调用父类的 selectPage 方法执行分页查询
        return selectPage(
                // 第一个参数：分页请求对象（包含 pageNo/pageSize）
                reqVO,
                // 第二个参数：构建查询条件（使用 Lambda 表达式）
                new LambdaQueryWrapperX<AiModelDO>()
                        // 如果 reqVO.name 不为空，添加 LIKE 模糊查询条件（模型名称）
                        .likeIfPresent(AiModelDO::getName, reqVO.getName())
                        // 如果 reqVO.model 不为空，添加 EQUAL 精确查询条件（模型标识）
                        .eqIfPresent(AiModelDO::getModel, reqVO.getModel())
                        // 如果 reqVO.platform 不为空，添加 EQUAL 精确查询条件（平台类型）
                        .eqIfPresent(AiModelDO::getPlatform, reqVO.getPlatform())
                        // 添加排序规则（按 sort 字段升序）
                        .orderByAsc(AiModelDO::getSort)
        );
    }

    default List<AiModelDO> selectListByStatusAndType(Integer status, Integer type,
                                                      @Nullable String platform) {
        return selectList(new LambdaQueryWrapperX<AiModelDO>()
                .eq(AiModelDO::getStatus, status)
                .eq(AiModelDO::getType, type)
                .eqIfPresent(AiModelDO::getPlatform, platform)
                .orderByAsc(AiModelDO::getSort));
    }

}
