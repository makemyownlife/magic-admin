package cn.javayong.magic.module.infra.framework.codegen.config;

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
     * 枚举 {@link cn.javayong.magic.module.infra.domain.enums.CodegenFrontTypeEnum#getType()}
     */
    @NotNull(message = "代码生成的前端类型不能为空")
    private Integer frontType;

    /**
     * 代码生成的 VO 类型
     *
     * 枚举 {@link cn.javayong.magic.module.infra.domain.enums.CodegenVOTypeEnum#getType()}
     */
    @NotNull(message = "代码生成的 VO 类型不能为空")
    private Integer voType;

    /**
     * 是否生成批量删除接口
     */
    @NotNull(message = "是否生成批量删除接口不能为空")
    private Boolean deleteBatchEnable;

    /**
     * 是否生成单元测试
     */
    @NotNull(message = "是否生成单元测试不能为空")
    private Boolean unitTestEnable;

}
