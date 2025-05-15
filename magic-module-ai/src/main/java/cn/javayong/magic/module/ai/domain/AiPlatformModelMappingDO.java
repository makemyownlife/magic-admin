package cn.javayong.magic.module.ai.domain;

import cn.javayong.magic.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * AI平台模型映射 DO
 */
@TableName("ai_platform_model_mapping")
@KeySequence("ai_platform_model_mapping_seq") // 序列，适用于Oracle等数据库
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiPlatformModelMappingDO extends BaseDO {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 平台ID
     * <p>
     * 关联 {@link AiPlatformDO#id}
     */
    private Integer platformId;

    /**
     * 模型ID
     */
    private Integer modelId;

    /**
     * 模型名称（冗余存储）
     */
    private String model;

    /**
     * 映射名称
     */
    private String mappingName;

}