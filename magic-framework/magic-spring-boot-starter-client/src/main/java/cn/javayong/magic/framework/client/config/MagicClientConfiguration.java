package cn.javayong.magic.framework.client.config;

import cn.javayong.magic.framework.redis.config.MagicRedisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;

@AutoConfiguration(after = MagicRedisAutoConfiguration.class)
public class MagicClientConfiguration {
}
