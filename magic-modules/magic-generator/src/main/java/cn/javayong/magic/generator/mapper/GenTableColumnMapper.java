package cn.javayong.magic.generator.mapper;

import cn.javayong.magic.generator.domain.GenTableColumn;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import cn.javayong.magic.common.mybatis.core.mapper.BaseMapperPlus;

/**
 * 业务字段 数据层
 *
 * @author Lion Li
 */
@InterceptorIgnore(dataPermission = "true", tenantLine = "true")
public interface GenTableColumnMapper extends BaseMapperPlus<GenTableColumn, GenTableColumn> {

}
