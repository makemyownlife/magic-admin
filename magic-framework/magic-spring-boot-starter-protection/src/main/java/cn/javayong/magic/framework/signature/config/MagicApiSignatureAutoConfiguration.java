package cn.javayong.magic.framework.signature.config;

import cn.javayong.magic.framework.client.config.MagicClientConfiguration;
import cn.javayong.magic.framework.client.core.adapter.ClientAdapter;
import cn.javayong.magic.framework.signature.core.aop.ApiSignatureAspect;
import cn.javayong.magic.framework.signature.core.redis.ApiSignatureRedisDAO;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * HTTP API 签名的自动配置类
 *
 * @author Zhougang
 */
@AutoConfiguration(after = MagicClientConfiguration.class)
public class MagicApiSignatureAutoConfiguration {

    @Bean
    public ApiSignatureAspect signatureAspect(ClientAdapter clientAdapter, ApiSignatureRedisDAO signatureRedisDAO) {
        return new ApiSignatureAspect(clientAdapter, signatureRedisDAO);
    }

    @Bean
    public ApiSignatureRedisDAO signatureRedisDAO(StringRedisTemplate stringRedisTemplate) {
        return new ApiSignatureRedisDAO(stringRedisTemplate);
    }

}
