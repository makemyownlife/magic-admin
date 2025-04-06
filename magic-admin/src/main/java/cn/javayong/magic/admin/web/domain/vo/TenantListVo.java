package cn.javayong.magic.admin.web.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import cn.javayong.magic.system.domain.vo.SysTenantVo;

/**
 * 租户列表
 *
 * @author Lion Li
 */
@Data
@AutoMapper(target = SysTenantVo.class)
public class TenantListVo {

    /**
     * 租户编号
     */
    private String tenantId;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 域名
     */
    private String domain;

}
