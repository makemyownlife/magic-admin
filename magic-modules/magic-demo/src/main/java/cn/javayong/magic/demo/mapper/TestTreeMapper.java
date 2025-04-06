package cn.javayong.magic.demo.mapper;

import cn.javayong.magic.common.mybatis.annotation.DataColumn;
import cn.javayong.magic.common.mybatis.annotation.DataPermission;
import cn.javayong.magic.common.mybatis.core.mapper.BaseMapperPlus;
import cn.javayong.magic.demo.domain.TestTree;
import cn.javayong.magic.demo.domain.vo.TestTreeVo;

/**
 * 测试树表Mapper接口
 *
 * @author Lion Li
 * @date 2021-07-26
 */
@DataPermission({
    @DataColumn(key = "deptName", value = "dept_id"),
    @DataColumn(key = "userName", value = "user_id")
})
public interface TestTreeMapper extends BaseMapperPlus<TestTree, TestTreeVo> {

}
