package cn.javayong.magic.framework.idempotent.core.redis;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 幂等 Redis DAO
 */
@AllArgsConstructor
public class IdempotentRedisDAO {

    /**
     * 幂等操作
     * <p>
     * KEY 格式：%s:idempotent:%s // 参数为 uuid
     * VALUE 格式：String
     * 过期时间：不固定
     */
    private static final String IDEMPOTENT = "%s:idempotent:%s";

    private final StringRedisTemplate redisTemplate;

    public Boolean setIfAbsent(String appName, String key, long timeout, TimeUnit timeUnit) {
        String redisKey = formatKey(appName, key);
        return redisTemplate.opsForValue().setIfAbsent(redisKey, "", timeout, timeUnit);
    }

    public void delete(String appName, String key) {
        String redisKey = formatKey(appName, key);
        redisTemplate.delete(redisKey);
    }

    private static String formatKey(String appName, String key) {
        return String.format(IDEMPOTENT, appName, key);
    }

}
