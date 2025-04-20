package cn.javayong.magic.module.system.domain.convert.user;

import cn.javayong.magic.framework.common.util.collection.CollectionUtils;
import cn.javayong.magic.framework.common.util.collection.MapUtils;
import cn.javayong.magic.framework.common.util.object.BeanUtils;
import cn.javayong.magic.module.system.domain.vo.DeptSimpleRespVO;
import cn.javayong.magic.module.system.domain.vo.PostSimpleRespVO;
import cn.javayong.magic.module.system.domain.vo.RoleSimpleRespVO;
import cn.javayong.magic.module.system.domain.vo.UserProfileRespVO;
import cn.javayong.magic.module.system.domain.vo.UserRespVO;
import cn.javayong.magic.module.system.domain.vo.UserSimpleRespVO;
import cn.javayong.magic.module.system.domain.DeptDO;
import cn.javayong.magic.module.system.domain.PostDO;
import cn.javayong.magic.module.system.domain.RoleDO;
import cn.javayong.magic.module.system.domain.AdminUserDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    default List<UserRespVO> convertList(List<AdminUserDO> list, Map<Long, DeptDO> deptMap) {
        return CollectionUtils.convertList(list, user -> convert(user, deptMap.get(user.getDeptId())));
    }

    default UserRespVO convert(AdminUserDO user, DeptDO dept) {
        UserRespVO userVO = BeanUtils.toBean(user, UserRespVO.class);
        if (dept != null) {
            userVO.setDeptName(dept.getName());
        }
        return userVO;
    }

    default List<UserSimpleRespVO> convertSimpleList(List<AdminUserDO> list, Map<Long, DeptDO> deptMap) {
        return CollectionUtils.convertList(list, user -> {
            UserSimpleRespVO userVO = BeanUtils.toBean(user, UserSimpleRespVO.class);
            MapUtils.findAndThen(deptMap, user.getDeptId(), dept -> userVO.setDeptName(dept.getName()));
            return userVO;
        });
    }

    default UserProfileRespVO convert(AdminUserDO user,
                                      List<RoleDO> userRoles,
                                      DeptDO dept,
                                      List<PostDO> posts) {
        UserProfileRespVO userVO = BeanUtils.toBean(user, UserProfileRespVO.class);
        userVO.setRoles(BeanUtils.toBean(userRoles, RoleSimpleRespVO.class));
        userVO.setDept(BeanUtils.toBean(dept, DeptSimpleRespVO.class));
        userVO.setPosts(BeanUtils.toBean(posts, PostSimpleRespVO.class));
        return userVO;
    }

}
