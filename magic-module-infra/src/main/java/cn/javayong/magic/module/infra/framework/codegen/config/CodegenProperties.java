package cn.javayong.magic.module.infra.framework.codegen.config;

import cn.javayong.magic.module.infra.domain.enums.CodegenFrontTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@ConfigurationProperties(prefix = "magic.codegen")
@Validated
@Data
public class CodegenProperties {

    /**
     * 生成的 Java 代码的基础包
     */
    @NotNull(message = "Java 代码的基础包不能为空")
    private String basePackage;

    /**
     * 数据库名数组
     */
    @NotEmpty(message = "数据库不能为空")
    private Collection<String> dbSchemas;

    /**
     * 代码生成的前端类型（默认）
     *
     * 枚举 {@link CodegenFrontTypeEnum#getType()}
     */
    @NotNull(message = "代码生成的前端类型不能为空")
    private Integer frontType;

    /**
     * 是否生成单元测试
     */
    @NotNull(message = "是否生成单元测试不能为空")
    private Boolean unitTestEnable;

}
