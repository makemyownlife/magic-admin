package cn.javayong.magic.module.ai.domain;

import cn.javayong.magic.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AI 平台配置 DO (baseUrl 模型映射)
 */
@TableName("ai_platform")
@KeySequence("ai_platform_seq")                // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiPlatformDO extends BaseDO {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 模型映射 JSON 格式
     */
    private String modelIds;

    /**
     * 平台配置名称
     */
    private String name;

    /**
     * api 请求 url
     */
    private String baseUrl;

    /**
     * 平台
     * <p>
     * 枚举 {@link cn.javayong.magic.module.ai.domain.enums.AiPlatformEnum}
     */
    private String platform;

    /**
     * 排序值
     */
    private Integer sort;

    /**
     * 状态
     */
    private Integer status;

    @Data
    public static class ModelMapping {

        // 模型 Id 对应 模型表 ai_model 表主键
        String modelId;

        // 标准模型名称 模型表 ai_model name 字段
        String model;

        // 目标模型参数 比如 标准模型名称是： gpt-3.5-turbo-0301，
        String MappingName;

    }

}
