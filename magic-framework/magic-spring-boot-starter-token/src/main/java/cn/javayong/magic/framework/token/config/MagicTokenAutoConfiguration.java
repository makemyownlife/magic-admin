package cn.javayong.magic.framework.token.config;

import cn.javayong.magic.framework.redis.config.MagicRedisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@AutoConfiguration(after = MagicRedisAutoConfiguration.class)
public class MagicTokenAutoConfiguration {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    

}
