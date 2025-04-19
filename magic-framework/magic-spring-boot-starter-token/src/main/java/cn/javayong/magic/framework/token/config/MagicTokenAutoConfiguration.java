package cn.javayong.magic.framework.token.config;

import cn.javayong.magic.framework.redis.config.MagicRedisAutoConfiguration;
import cn.javayong.magic.framework.token.core.SecurityTokenService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

@AutoConfiguration(after = MagicRedisAutoConfiguration.class)
public class MagicTokenAutoConfiguration {

    @Bean(value = "securityTokenService")
    public SecurityTokenService createSecurityTokenService(StringRedisTemplate stringRedisTemplate) {
        return new SecurityTokenService(stringRedisTemplate);
    }

}
