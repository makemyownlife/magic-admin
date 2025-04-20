package cn.javayong.magic.module.system.job;

import cn.javayong.magic.framework.quartz.core.handler.JobHandler;
import cn.javayong.magic.framework.tenant.core.context.TenantContextHolder;
import cn.javayong.magic.framework.tenant.core.job.TenantJob;
import cn.javayong.magic.module.system.domain.AdminUserDO;
import cn.javayong.magic.module.system.mapper.AdminUserMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class DemoJob implements JobHandler {

    @Resource
    private AdminUserMapper adminUserMapper;

    @Override
    @TenantJob // 标记多租户
    public String execute(String param) {
        System.out.println("当前租户：" + TenantContextHolder.getTenantId());
        List<AdminUserDO> users = adminUserMapper.selectList();
        return "用户数量：" + users.size();
    }

}
