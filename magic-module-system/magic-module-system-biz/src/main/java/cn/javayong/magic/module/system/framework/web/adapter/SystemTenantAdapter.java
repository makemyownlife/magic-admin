package cn.javayong.magic.module.system.framework.web.adapter;

import cn.javayong.magic.framework.tenant.core.adapter.TenantAdapter;
import cn.javayong.magic.module.system.service.TenantService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 多租户的 API 实现类
 *

 */
@Service
public class SystemTenantAdapter implements TenantAdapter {

    @Resource
    private TenantService tenantService;

    @Override
    public List<Long> getTenantIdList() {
        return tenantService.getTenantIdList();
    }

    @Override
    public void validateTenant(Long id) {
        tenantService.validTenant(id);
    }

}
