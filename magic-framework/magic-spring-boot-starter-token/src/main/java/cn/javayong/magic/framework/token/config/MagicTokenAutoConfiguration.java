package cn.javayong.magic.framework.token.config;

import cn.javayong.magic.framework.client.core.adapter.ClientAdapter;
import cn.javayong.magic.framework.redis.config.MagicRedisAutoConfiguration;
import cn.javayong.magic.framework.token.core.service.SecurityTokenService;
import cn.javayong.magic.framework.token.core.service.impl.SecurityTokenServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

@AutoConfiguration(after = MagicRedisAutoConfiguration.class)
public class MagicTokenAutoConfiguration {

    @Bean(value = "securityTokenService")
    public SecurityTokenService createSecurityTokenService(ClientAdapter clientAdapter, StringRedisTemplate stringRedisTemplate) {
        return new SecurityTokenServiceImpl(clientAdapter, stringRedisTemplate);
    }

}
