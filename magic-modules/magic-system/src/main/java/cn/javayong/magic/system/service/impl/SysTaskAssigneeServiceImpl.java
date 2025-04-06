package cn.javayong.magic.system.service.impl;

import cn.hutool.core.convert.Convert;
import lombok.RequiredArgsConstructor;
import cn.javayong.magic.common.core.domain.dto.TaskAssigneeDTO;
import cn.javayong.magic.common.core.domain.model.TaskAssigneeBody;
import cn.javayong.magic.common.core.service.TaskAssigneeService;
import cn.javayong.magic.common.mybatis.core.page.PageQuery;
import cn.javayong.magic.common.mybatis.core.page.TableDataInfo;
import cn.javayong.magic.system.domain.bo.SysDeptBo;
import cn.javayong.magic.system.domain.bo.SysPostBo;
import cn.javayong.magic.system.domain.bo.SysRoleBo;
import cn.javayong.magic.system.domain.bo.SysUserBo;
import cn.javayong.magic.system.domain.vo.SysDeptVo;
import cn.javayong.magic.system.domain.vo.SysPostVo;
import cn.javayong.magic.system.domain.vo.SysRoleVo;
import cn.javayong.magic.system.domain.vo.SysUserVo;
import cn.javayong.magic.system.service.ISysDeptService;
import cn.javayong.magic.system.service.ISysPostService;
import cn.javayong.magic.system.service.ISysRoleService;
import cn.javayong.magic.system.service.ISysUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 工作流设计器获取任务执行人
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class SysTaskAssigneeServiceImpl implements TaskAssigneeService {

    // 上级Service注入下级Service 其他Service永远不可能注入当前类 避免循环注入
    private final ISysPostService postService;
    private final ISysDeptService deptService;
    private final ISysUserService userService;
    private final ISysRoleService roleService;

    /**
     * 查询角色并返回任务指派的列表，支持分页
     *
     * @param taskQuery 查询条件
     * @return 办理人
     */
    @Override
    public TaskAssigneeDTO selectRolesByTaskAssigneeList(TaskAssigneeBody taskQuery) {
        PageQuery pageQuery = new PageQuery(taskQuery.getPageSize(), taskQuery.getPageNum());
        SysRoleBo bo = new SysRoleBo();
        bo.setRoleName(taskQuery.getHandlerCode());
        bo.setRoleKey(taskQuery.getHandlerName());
        Map<String, Object> params = bo.getParams();
        params.put("beginTime", taskQuery.getBeginTime());
        params.put("endTime", taskQuery.getEndTime());
        TableDataInfo<SysRoleVo> page = roleService.selectPageRoleList(bo, pageQuery);
        // 使用封装的字段映射方法进行转换
        List<TaskAssigneeDTO.TaskHandler> handlers = TaskAssigneeDTO.convertToHandlerList(page.getRows(),
            SysRoleVo::getRoleId, SysRoleVo::getRoleKey, SysRoleVo::getRoleName, null, SysRoleVo::getCreateTime);
        return new TaskAssigneeDTO(page.getTotal(), handlers);
    }

    /**
     * 查询岗位并返回任务指派的列表，支持分页
     *
     * @param taskQuery 查询条件
     * @return 办理人
     */
    @Override
    public TaskAssigneeDTO selectPostsByTaskAssigneeList(TaskAssigneeBody taskQuery) {
        PageQuery pageQuery = new PageQuery(taskQuery.getPageSize(), taskQuery.getPageNum());
        SysPostBo bo = new SysPostBo();
        bo.setPostCategory(taskQuery.getHandlerCode());
        bo.setPostName(taskQuery.getHandlerName());
        Map<String, Object> params = bo.getParams();
        params.put("beginTime", taskQuery.getBeginTime());
        params.put("endTime", taskQuery.getEndTime());
        bo.setBelongDeptId(Convert.toLong(taskQuery.getGroupId()));
        TableDataInfo<SysPostVo> page = postService.selectPagePostList(bo, pageQuery);
        // 使用封装的字段映射方法进行转换
        List<TaskAssigneeDTO.TaskHandler> handlers = TaskAssigneeDTO.convertToHandlerList(page.getRows(),
            SysPostVo::getPostId, SysPostVo::getPostCategory, SysPostVo::getPostName, SysPostVo::getDeptId, SysPostVo::getCreateTime);
        return new TaskAssigneeDTO(page.getTotal(), handlers);
    }

    /**
     * 查询部门并返回任务指派的列表，支持分页
     *
     * @param taskQuery 查询条件
     * @return 办理人
     */
    @Override
    public TaskAssigneeDTO selectDeptsByTaskAssigneeList(TaskAssigneeBody taskQuery) {
        PageQuery pageQuery = new PageQuery(taskQuery.getPageSize(), taskQuery.getPageNum());
        SysDeptBo bo = new SysDeptBo();
        bo.setDeptCategory(taskQuery.getHandlerCode());
        bo.setDeptName(taskQuery.getHandlerName());
        Map<String, Object> params = bo.getParams();
        params.put("beginTime", taskQuery.getBeginTime());
        params.put("endTime", taskQuery.getEndTime());
        bo.setBelongDeptId(Convert.toLong(taskQuery.getGroupId()));
        TableDataInfo<SysDeptVo> page = deptService.selectPageDeptList(bo, pageQuery);
        // 使用封装的字段映射方法进行转换
        List<TaskAssigneeDTO.TaskHandler> handlers = TaskAssigneeDTO.convertToHandlerList(page.getRows(),
            SysDeptVo::getDeptId, SysDeptVo::getDeptCategory, SysDeptVo::getDeptName, SysDeptVo::getParentId, SysDeptVo::getCreateTime);
        return new TaskAssigneeDTO(page.getTotal(), handlers);
    }


    /**
     * 查询用户并返回任务指派的列表，支持分页
     *
     * @param taskQuery 查询条件
     * @return 办理人
     */
    @Override
    public TaskAssigneeDTO selectUsersByTaskAssigneeList(TaskAssigneeBody taskQuery) {
        PageQuery pageQuery = new PageQuery(taskQuery.getPageSize(), taskQuery.getPageNum());
        SysUserBo bo = new SysUserBo();
        bo.setUserName(taskQuery.getHandlerCode());
        bo.setNickName(taskQuery.getHandlerName());
        Map<String, Object> params = bo.getParams();
        params.put("beginTime", taskQuery.getBeginTime());
        params.put("endTime", taskQuery.getEndTime());
        bo.setDeptId(Convert.toLong(taskQuery.getGroupId()));
        TableDataInfo<SysUserVo> page = userService.selectPageUserList(bo, pageQuery);
        // 使用封装的字段映射方法进行转换
        List<TaskAssigneeDTO.TaskHandler> handlers = TaskAssigneeDTO.convertToHandlerList(page.getRows(),
            SysUserVo::getUserId, SysUserVo::getUserName, SysUserVo::getNickName, SysUserVo::getDeptId, SysUserVo::getCreateTime);
        return new TaskAssigneeDTO(page.getTotal(), handlers);
    }

}
