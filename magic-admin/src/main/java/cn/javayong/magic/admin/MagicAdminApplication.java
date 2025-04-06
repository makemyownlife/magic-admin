package cn.javayong.magic.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动程序
 * @author zhangyong
 */
@SpringBootApplication
@ComponentScan(basePackages = {"cn.javayong"})
public class MagicAdminApplication {

    private static Logger logger = LoggerFactory.getLogger(MagicAdminApplication.class);

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MagicAdminApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        logger.info("(♥◠‿◠)ﾉﾞ magic-admin 启动成功   ლ(´ڡ`ლ)ﾞ");
    }

}
