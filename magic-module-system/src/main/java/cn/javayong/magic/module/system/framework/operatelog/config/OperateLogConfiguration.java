package cn.javayong.magic.module.system.framework.operatelog.config;

import cn.javayong.magic.module.system.framework.operatelog.core.service.LogRecordServiceImpl;
import com.mzt.logapi.service.ILogRecordService;
import com.mzt.logapi.starter.annotation.EnableLogRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 操作日志配置类
 */
@EnableLogRecord(tenant = "") // 貌似用不上 tenant 这玩意给个空好啦 此处注解非常关键
@Configuration(proxyBeanMethods = false)
@Slf4j
public class OperateLogConfiguration {

    @Bean
    @Primary
    public ILogRecordService iLogRecordServiceImpl() {
        log.info("创建 ILogRecordService ");
        return new LogRecordServiceImpl();
    }

}
