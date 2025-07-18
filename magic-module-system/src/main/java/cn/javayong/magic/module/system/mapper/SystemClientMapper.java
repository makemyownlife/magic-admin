package cn.javayong.magic.module.system.mapper;

import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.javayong.magic.framework.mybatis.core.mapper.BaseMapperX;
import cn.javayong.magic.module.system.domain.dataobject.SystemClientDO;
import cn.javayong.magic.module.system.domain.vo.SystemClientPageReqVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统客户端 Mapper
 *
 * @author admin
 */
@Mapper
public interface SystemClientMapper extends BaseMapperX<SystemClientDO> {

    default PageResult<SystemClientDO> selectPage(SystemClientPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SystemClientDO>()
                .eqIfPresent(SystemClientDO::getClientKey, reqVO.getClientKey())
                .eqIfPresent(SystemClientDO::getClientSecret, reqVO.getClientSecret())
                .eqIfPresent(SystemClientDO::getGrantType, reqVO.getGrantType())
                .eqIfPresent(SystemClientDO::getDeviceType, reqVO.getDeviceType())
                .eqIfPresent(SystemClientDO::getAccessTimeout, reqVO.getAccessTimeout())
                .eqIfPresent(SystemClientDO::getRefreshTimeout, reqVO.getRefreshTimeout())
                .eqIfPresent(SystemClientDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(SystemClientDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(SystemClientDO::getId));
    }

}