package cn.javayong.magic.module.infra.dal.mysql.demo.demo03;

import cn.javayong.magic.framework.common.pojo.PageResult;
import cn.javayong.magic.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.javayong.magic.framework.mybatis.core.mapper.BaseMapperX;
import cn.javayong.magic.module.infra.dal.dataobject.demo.demo03.Demo03StudentDO;
import org.apache.ibatis.annotations.Mapper;
import cn.javayong.magic.module.infra.controller.admin.demo.demo03.vo.*;

/**
 * 学生 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface Demo03StudentMapper extends BaseMapperX<Demo03StudentDO> {

    default PageResult<Demo03StudentDO> selectPage(Demo03StudentPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<Demo03StudentDO>()
                .likeIfPresent(Demo03StudentDO::getName, reqVO.getName())
                .eqIfPresent(Demo03StudentDO::getSex, reqVO.getSex())
                .eqIfPresent(Demo03StudentDO::getDescription, reqVO.getDescription())
                .betweenIfPresent(Demo03StudentDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(Demo03StudentDO::getId));
    }

}