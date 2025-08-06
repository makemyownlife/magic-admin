package cn.javayong.magic.framework.idempotent.config;

import cn.javayong.magic.framework.idempotent.core.aop.IdempotentAspect;
import cn.javayong.magic.framework.idempotent.core.keyresolver.impl.DefaultIdempotentKeyResolver;
import cn.javayong.magic.framework.idempotent.core.keyresolver.impl.ExpressionIdempotentKeyResolver;
import cn.javayong.magic.framework.idempotent.core.keyresolver.IdempotentKeyResolver;
import cn.javayong.magic.framework.idempotent.core.keyresolver.impl.UserIdempotentKeyResolver;
import cn.javayong.magic.framework.idempotent.core.redis.IdempotentRedisDAO;
import cn.javayong.magic.framework.token.config.MagicTokenAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@AutoConfiguration(after = MagicTokenAutoConfiguration.class)
public class MagicIdempotentConfiguration {

    @Bean
    public IdempotentAspect idempotentAspect(List<IdempotentKeyResolver> keyResolvers, IdempotentRedisDAO idempotentRedisDAO) {
        return new IdempotentAspect(keyResolvers, idempotentRedisDAO);
    }

    @Bean
    public IdempotentRedisDAO idempotentRedisDAO(StringRedisTemplate stringRedisTemplate) {
        return new IdempotentRedisDAO(stringRedisTemplate);
    }

    // ========== 各种 IdempotentKeyResolver Bean ==========

    @Bean
    public DefaultIdempotentKeyResolver defaultIdempotentKeyResolver() {
        return new DefaultIdempotentKeyResolver();
    }

    @Bean
    public UserIdempotentKeyResolver userIdempotentKeyResolver() {
        return new UserIdempotentKeyResolver();
    }

    @Bean
    public ExpressionIdempotentKeyResolver expressionIdempotentKeyResolver() {
        return new ExpressionIdempotentKeyResolver();
    }

}
