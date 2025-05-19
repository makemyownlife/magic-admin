package cn.javayong.magic.module.ai.mapper;

import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.framework.mybatis.core.mapper.BaseMapperX;
import cn.javayong.magic.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.javayong.magic.module.ai.domain.AiModelDO;
import cn.javayong.magic.module.ai.domain.AiOneApiTokenDO;
import cn.javayong.magic.module.ai.domain.vo.AiModelPageReqVO;
import cn.javayong.magic.module.ai.domain.vo.AiOneApiTokenPageReqVO;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.Nullable;
import java.util.List;

/**
 * API ONE API TOKEN  Mapper
 */
@Mapper
public interface AiOneApiTokenMapper extends BaseMapperX<AiOneApiTokenDO> {

    /**
     * 分页查询 AI oneapi token 数据（仅按创建时间降序排序）
     * @param reqVO 分页查询请求参数（仅包含分页参数）
     * @return 分页结果对象（包含数据列表和分页信息）
     */
    default PageResult<AiOneApiTokenDO> selectPage(AiOneApiTokenPageReqVO reqVO) {
        // 调用父类的 selectPage 方法执行分页查询
        return selectPage(
                // 第一个参数：分页请求对象（包含 pageNo/pageSize）
                reqVO,
                // 第二个参数：构建查询条件（使用 Lambda 表达式）
                new LambdaQueryWrapperX<AiOneApiTokenDO>()
                        // 添加排序规则（按 createTime 字段降序）
                        .orderByDesc(AiOneApiTokenDO::getCreateTime)
        );
    }
}
