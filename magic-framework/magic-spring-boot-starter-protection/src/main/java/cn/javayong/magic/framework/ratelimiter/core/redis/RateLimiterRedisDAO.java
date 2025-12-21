package cn.javayong.magic.framework.ratelimiter.core.redis;

import lombok.AllArgsConstructor;
import org.redisson.api.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 限流 Redis DAO
 */
@AllArgsConstructor
public class RateLimiterRedisDAO {

    /**
     * Redis 限流器 Key 模板
     *
     * @param appName 应用名称（如 "order-service"）
     * @param id      限流器唯一标识（如 UUID 或用户ID）
     * 最终格式：{appName}:rate_limiter:{id}
     */
    private static final String RATE_LIMITER_KEY_TEMPLATE = "%s:rate_limiter:%s";

    private final RedissonClient redissonClient;

    public Boolean tryAcquire(String appName, String key, int count, int time, TimeUnit timeUnit) {
        // 1. 获得 RRateLimiter，并设置 rate 速率
        RRateLimiter rateLimiter = getRRateLimiter(appName, key, count, time, timeUnit);
        // 2. 尝试获取 1 个
        return rateLimiter.tryAcquire();
    }

    private static String formatKey(String appName, String key) {
        return String.format(RATE_LIMITER_KEY_TEMPLATE, appName, key);
    }

    private RRateLimiter getRRateLimiter(String appName, String key, long count, int time, TimeUnit timeUnit) {
        String redisKey = formatKey(appName, key);
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(redisKey);
        long rateInterval = timeUnit.toSeconds(time);
        // 1. 如果不存在，设置 rate 速率
        RateLimiterConfig config = rateLimiter.getConfig();
        if (config == null) {
            rateLimiter.trySetRate(RateType.OVERALL, count, rateInterval, RateIntervalUnit.SECONDS);
            rateLimiter.expire(rateInterval, TimeUnit.SECONDS);
            return rateLimiter;
        }
        // 2. 如果存在，并且配置相同，则直接返回
        if (config.getRateType() == RateType.OVERALL
                && Objects.equals(config.getRate(), count)
                && Objects.equals(config.getRateInterval(), TimeUnit.SECONDS.toMillis(rateInterval))) {
            return rateLimiter;
        }
        // 3. 如果存在，并且配置不同，则进行新建
        rateLimiter.setRate(RateType.OVERALL, count, rateInterval, RateIntervalUnit.SECONDS);
        rateLimiter.expire(rateInterval, TimeUnit.SECONDS);
        return rateLimiter;
    }

}
