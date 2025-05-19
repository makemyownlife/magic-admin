package cn.javayong.magic.module.ai.domain;

import cn.javayong.magic.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * AI OneAPI 令牌实体类
 */
@TableName("ai_oneapi_token")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AiOneApiTokenDO extends BaseDO {

    /**
     * 主键ID (雪花算法)
     */
    private Long id;

    /**
     *  模型列表
     */
    private String modelIds;

    /**
     * 令牌名称
     */
    private String name;

    /**
     * 令牌值 (实际API密钥)
     */
    private String token;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

}
