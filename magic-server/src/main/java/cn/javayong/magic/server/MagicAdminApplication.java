package cn.javayong.magic.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目的启动类
 */
@SuppressWarnings("SpringComponentScan") // 忽略 IDEA 无法识别 ${magic.info.base-package}
@SpringBootApplication(scanBasePackages = {"${magic.info.base-package}.server", "${magic.info.base-package}.module"})
public class MagicAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(MagicAdminApplication.class, args);
    }

}
