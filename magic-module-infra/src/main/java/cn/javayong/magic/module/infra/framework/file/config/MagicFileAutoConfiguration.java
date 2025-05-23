package cn.javayong.magic.module.infra.framework.file.config;

import cn.javayong.magic.module.infra.framework.file.core.client.FileClientFactory;
import cn.javayong.magic.module.infra.framework.file.core.client.FileClientFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文件配置类
 *

 */
@Configuration(proxyBeanMethods = false)
public class MagicFileAutoConfiguration {

    @Bean
    public FileClientFactory fileClientFactory() {
        return new FileClientFactoryImpl();
    }

}
