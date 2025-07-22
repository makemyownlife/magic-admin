package cn.javayong.magic.module.system.framework.web.adapter;

import cn.javayong.magic.framework.token.core.adapter.ClientAdapter;
import cn.javayong.magic.framework.token.core.dto.SecurityClientDTO;
import cn.javayong.magic.module.system.domain.dataobject.SystemClientDO;
import cn.javayong.magic.module.system.service.SystemClientService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 重点：不同客户端登录获取到的token不同与其他端不互通(例如: app登录获取到的token无法用于pc端接口查询)
 **/
@Component
@Slf4j
public class SystemClientAdapter implements ClientAdapter {

    @Resource
    private SystemClientService systemClientService;

    // 默认 登录客户端平台  1： admin PC 后台
    final private static Long DEFAULT_CLINET_ID = 1L;

    @Value("#{'${spring.application.name}'}")  // 明确使用 SpEL
    private String appName;

    @Override
    public SecurityClientDTO getClient() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        SystemClientDO systemClientDO = null;
        String clientKey = request.getHeader("x-client-key");

        if (StringUtils.isEmpty(clientKey)) {
            // 若 客户端没有传输 clientKey 则默认为 1： admin PC 后台
            systemClientDO = systemClientService.getClientByIdFromCache(DEFAULT_CLINET_ID);
        } else {
            systemClientDO = systemClientService.getSystemClientByClientKeyFromCache(clientKey);
        }

        if (systemClientDO == null) {
            log.error("[SystemClientAdapter] 获取客户端信息失败，请检查客户端信息 clientKey:" + clientKey);
            return null;
        }

        SecurityClientDTO securityClientDTO = new SecurityClientDTO();
        securityClientDTO.setNamespace(appName);
        securityClientDTO.setClientId(systemClientDO.getId());
        securityClientDTO.setClientKey(systemClientDO.getClientKey());
        securityClientDTO.setClientSecret(systemClientDO.getClientSecret());
        securityClientDTO.setAccessTimeout(systemClientDO.getAccessTimeout());
        securityClientDTO.setRefreshTimeout(systemClientDO.getRefreshTimeout());

        return securityClientDTO;
    }

}
