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
     * 幂等性 Redis Key 模板（格式：{appName}:idempotent:{uniqueId}）
     *
     * @param appName  应用名称（如 "order-service"），用于多服务隔离
     * @param uniqueId 唯一请求标识（如 UUID、业务ID），确保幂等性
     *
     * 示例：
     *   String key = String.format(IDEMPOTENT_KEY_TEMPLATE, "payment", "txn-789abc");
     *   // 生成："payment:idempotent:txn-789abc"
     */
    private static final String IDEMPOTENT_KEY_TEMPLATE = "%s:idempotent:%s";

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
        return String.format(IDEMPOTENT_KEY_TEMPLATE, appName, key);
    }

}
