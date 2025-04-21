package cn.javayong.magic.framework.tenant.config;

import cn.javayong.magic.framework.common.enums.WebFilterOrderEnum;
import cn.javayong.magic.framework.mybatis.core.util.MyBatisUtils;
import cn.javayong.magic.framework.redis.config.MagicCacheProperties;
import cn.javayong.magic.framework.tenant.core.aop.TenantIgnoreAspect;
import cn.javayong.magic.framework.tenant.core.db.TenantDatabaseInterceptor;
import cn.javayong.magic.framework.tenant.core.job.TenantJobAspect;
import cn.javayong.magic.framework.tenant.core.mq.rabbitmq.TenantRabbitMQInitializer;
import cn.javayong.magic.framework.tenant.core.mq.redis.TenantRedisMessageInterceptor;
import cn.javayong.magic.framework.tenant.core.mq.rocketmq.TenantRocketMQInitializer;
import cn.javayong.magic.framework.tenant.core.redis.TenantRedisCacheManager;
import cn.javayong.magic.framework.tenant.core.security.TenantSecurityWebFilter;
import cn.javayong.magic.framework.tenant.core.service.TenantFrameworkService;
import cn.javayong.magic.framework.tenant.core.service.TenantFrameworkServiceImpl;
import cn.javayong.magic.framework.tenant.core.web.TenantContextWebFilter;
import cn.javayong.magic.framework.web.config.WebProperties;
import cn.javayong.magic.framework.web.core.handler.GlobalExceptionHandler;
import cn.javayong.magic.framework.tenant.core.adapter.TenantAdapter;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.BatchStrategies;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;

@AutoConfiguration
@ConditionalOnProperty(prefix = "magic.tenant", value = "enable", matchIfMissing = true) // 允许使用 magic.tenant.enable=false 禁用多租户
@EnableConfigurationProperties(TenantProperties.class)
public class MagicTenantAutoConfiguration {

    @Bean
    public TenantFrameworkService tenantFrameworkService(TenantAdapter tenantAdapter) {
        return new TenantFrameworkServiceImpl(tenantAdapter);
    }

    // ========== AOP ==========

    @Bean
    public TenantIgnoreAspect tenantIgnoreAspect() {
        return new TenantIgnoreAspect();
    }

    // ========== DB ==========

    @Bean
    public TenantLineInnerInterceptor tenantLineInnerInterceptor(TenantProperties properties,
                                                                 MybatisPlusInterceptor interceptor) {
        TenantLineInnerInterceptor inner = new TenantLineInnerInterceptor(new TenantDatabaseInterceptor(properties));
        // 添加到 interceptor 中
        // 需要加在首个，主要是为了在分页插件前面。这个是 MyBatis Plus 的规定
        MyBatisUtils.addInterceptor(interceptor, inner, 0);
        return inner;
    }

    // ========== WEB ==========

    @Bean
    public FilterRegistrationBean<TenantContextWebFilter> tenantContextWebFilter() {
        FilterRegistrationBean<TenantContextWebFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TenantContextWebFilter());
        registrationBean.setOrder(WebFilterOrderEnum.TENANT_CONTEXT_FILTER);
        return registrationBean;
    }

    // ========== Security ==========

    @Bean
    public FilterRegistrationBean<TenantSecurityWebFilter> tenantSecurityWebFilter(TenantProperties tenantProperties,
                                                                                   WebProperties webProperties,
                                                                                   GlobalExceptionHandler globalExceptionHandler,
                                                                                   TenantFrameworkService tenantFrameworkService) {
        FilterRegistrationBean<TenantSecurityWebFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TenantSecurityWebFilter(tenantProperties, webProperties,
                globalExceptionHandler, tenantFrameworkService));
        registrationBean.setOrder(WebFilterOrderEnum.TENANT_SECURITY_FILTER);
        return registrationBean;
    }

    // ========== MQ ==========

    @Bean
    public TenantRedisMessageInterceptor tenantRedisMessageInterceptor() {
        return new TenantRedisMessageInterceptor();
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.amqp.rabbit.core.RabbitTemplate")
    public TenantRabbitMQInitializer tenantRabbitMQInitializer() {
        return new TenantRabbitMQInitializer();
    }

    @Bean
    @ConditionalOnClass(name = "org.apache.rocketmq.spring.core.RocketMQTemplate")
    public TenantRocketMQInitializer tenantRocketMQInitializer() {
        return new TenantRocketMQInitializer();
    }

    // ========== Job ==========

    @Bean
    public TenantJobAspect tenantJobAspect(TenantFrameworkService tenantFrameworkService) {
        return new TenantJobAspect(tenantFrameworkService);
    }

    // ========== Redis ==========

    @Bean
    @Primary // 引入租户时，tenantRedisCacheManager 为主 Bean
    public RedisCacheManager tenantRedisCacheManager(RedisTemplate<String, Object> redisTemplate,
                                                     RedisCacheConfiguration redisCacheConfiguration,
                                                     MagicCacheProperties magicCacheProperties,
                                                     TenantProperties tenantProperties) {
        // 创建 RedisCacheWriter 对象
        RedisConnectionFactory connectionFactory = Objects.requireNonNull(redisTemplate.getConnectionFactory());
        RedisCacheWriter cacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory,
                BatchStrategies.scan(magicCacheProperties.getRedisScanBatchSize()));
        // 创建 TenantRedisCacheManager 对象
        return new TenantRedisCacheManager(cacheWriter, redisCacheConfiguration, tenantProperties.getIgnoreCaches());
    }

}
