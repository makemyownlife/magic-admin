package cn.javayong.magic.module.system.mapper;

import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.framework.mybatis.core.mapper.BaseMapperX;
import cn.javayong.magic.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.javayong.magic.module.system.domain.vo.TenantPackagePageReqVO;
import cn.javayong.magic.module.system.domain.TenantPackageDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 租户套餐 Mapper
 *

 */
@Mapper
public interface TenantPackageMapper extends BaseMapperX<TenantPackageDO> {

    default PageResult<TenantPackageDO> selectPage(TenantPackagePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TenantPackageDO>()
                .likeIfPresent(TenantPackageDO::getName, reqVO.getName())
                .eqIfPresent(TenantPackageDO::getStatus, reqVO.getStatus())
                .likeIfPresent(TenantPackageDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(TenantPackageDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(TenantPackageDO::getId));
    }

    default List<TenantPackageDO> selectListByStatus(Integer status) {
        return selectList(TenantPackageDO::getStatus, status);
    }

    default TenantPackageDO selectByName(String name) {
        return selectOne(TenantPackageDO::getName, name);
    }
}
