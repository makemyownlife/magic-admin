package cn.javayong.magic.module.system.job;

import cn.javayong.magic.framework.quartz.core.handler.JobHandler;
import cn.javayong.magic.module.system.domain.dataobject.AdminUserDO;
import cn.javayong.magic.module.system.mapper.AdminUserMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class DemoJob implements JobHandler {

    @Resource
    private AdminUserMapper adminUserMapper;

    @Override
    public String execute(String param) {
        List<AdminUserDO> users = adminUserMapper.selectList();
        return "用户数量：" + users.size();
    }

}
