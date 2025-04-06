package cn.javayong.magic.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import cn.javayong.magic.common.mybatis.annotation.DataColumn;
import cn.javayong.magic.common.mybatis.annotation.DataPermission;
import cn.javayong.magic.common.mybatis.core.mapper.BaseMapperPlus;
import cn.javayong.magic.system.domain.SysPost;
import cn.javayong.magic.system.domain.vo.SysPostVo;

import java.util.List;

/**
 * 岗位信息 数据层
 *
 * @author Lion Li
 */
public interface SysPostMapper extends BaseMapperPlus<SysPost, SysPostVo> {

    /**
     * 分页查询岗位列表
     *
     * @param page         分页对象
     * @param queryWrapper 查询条件
     * @return 包含岗位信息的分页结果
     */
    @DataPermission({
        @DataColumn(key = "deptName", value = "dept_id"),
        @DataColumn(key = "userName", value = "create_by")
    })
    Page<SysPostVo> selectPagePostList(@Param("page") Page<SysPostVo> page, @Param(Constants.WRAPPER) Wrapper<SysPost> queryWrapper);

    /**
     * 查询用户所属岗位组
     *
     * @param userId 用户ID
     * @return 结果
     */
    List<SysPostVo> selectPostsByUserId(Long userId);

}
