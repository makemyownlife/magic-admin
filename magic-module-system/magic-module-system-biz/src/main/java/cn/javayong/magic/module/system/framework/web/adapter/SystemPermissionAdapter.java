package cn.javayong.magic.module.system.framework.web.adapter;

import cn.javayong.magic.framework.security.core.adapter.PermissionAdapter;
import cn.javayong.magic.framework.security.core.dto.DeptDataPermissionRespDTO;
import cn.javayong.magic.module.system.service.PermissionService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;

/**
 * 权限 API 实现类
 *

 */
@Component
public class SystemPermissionAdapter implements PermissionAdapter {

    @Resource
    private PermissionService permissionService;

    public Set<Long> getUserRoleIdListByRoleIds(Collection<Long> roleIds) {
        return permissionService.getUserRoleIdListByRoleId(roleIds);
    }

    public boolean hasAnyPermissions(Long userId, String... permissions) {
        return permissionService.hasAnyPermissions(userId, permissions);
    }

    public boolean hasAnyRoles(Long userId, String... roles) {
        return permissionService.hasAnyRoles(userId, roles);
    }

    public DeptDataPermissionRespDTO getDeptDataPermission(Long userId) {
        return permissionService.getDeptDataPermission(userId);
    }

}
