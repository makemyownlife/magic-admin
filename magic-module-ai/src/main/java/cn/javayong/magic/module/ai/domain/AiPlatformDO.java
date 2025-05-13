package cn.javayong.magic.module.ai.domain;

import cn.javayong.magic.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AI 平台配置 DO (baseUrl )
 */
@TableName("ai_platform")
@KeySequence("ai_platform_seq")   // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
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
     * 平台
     *
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

}
