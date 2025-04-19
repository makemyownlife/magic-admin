package cn.javayong.magic.framework.token.core;

import org.springframework.data.redis.core.StringRedisTemplate;

public class SecurityTokenService {

    private StringRedisTemplate stringRedisTemplate;

    public SecurityTokenService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }


}
