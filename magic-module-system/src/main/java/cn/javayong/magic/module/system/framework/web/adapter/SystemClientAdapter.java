package cn.javayong.magic.module.system.framework.web.adapter;

import cn.javayong.magic.framework.token.core.adapter.ClientAdapter;
import cn.javayong.magic.framework.token.core.dto.SecurityClientDTO;
import cn.javayong.magic.module.system.service.client.SystemClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 重点：不同客户端登录获取到的token不同与其他端不互通(例如: app登录获取到的token无法用于pc端接口查询)
 **/
@Component
@Slf4j
public class SystemClientAdapter implements ClientAdapter {

    @Resource
    private SystemClientService systemClientService;

    // 默认 登录客户端平台  1： admin后台
    final private static Long DEFAULT_CLINET_ID = 1L;

    @Override
    public SecurityClientDTO getClient() {
        // 获取当前请求客户端
        systemClientService.getSystemClientByClientKeyFromCache("");
        return null;
    }

}
