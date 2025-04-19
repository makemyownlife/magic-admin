package cn.javayong.magic.framework.token.config;

import cn.javayong.magic.framework.redis.config.MagicRedisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;

@AutoConfiguration(after = MagicRedisAutoConfiguration.class)
public class MagicTokenAutoConfiguration {



}
