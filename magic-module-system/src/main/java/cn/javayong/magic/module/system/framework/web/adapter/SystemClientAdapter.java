package cn.javayong.magic.module.system.framework.web.adapter;

import cn.javayong.magic.framework.token.core.adapter.ClientAdapter;
import cn.javayong.magic.framework.token.core.dto.SecurityClientDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SystemClientAdapter implements ClientAdapter {

    // 默认 登录客户端平台  1： admin后台
    final private static Long DEFAULT_CLINET_ID = 1L;

    @Override
    public SecurityClientDTO getClient() {
        return null;
    }

}
