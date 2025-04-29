package cn.javayong.magic.idgenerator.config;

import cn.javayong.magic.framework.redis.config.MagicRedisAutoConfiguration;
import cn.javayong.magic.idgenerator.core.service.IdGeneratorService;
import cn.javayong.magic.idgenerator.core.service.impl.RedisIdGeneratorServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * ID 生成器 (通过 REDIS 自增实现)
 */
@AutoConfiguration(after = MagicRedisAutoConfiguration.class)
public class MagicIdGeneratorAutoConfiguration {

    @Bean(value = "idGeneratorService")
    public IdGeneratorService createSecurityTokenService(StringRedisTemplate stringRedisTemplate) {
        IdGeneratorService idGeneratorService = new RedisIdGeneratorServiceImpl(stringRedisTemplate);
        return idGeneratorService;
    }

}
