package cn.javayong.magic.framework.token.core.dto;

import cn.javayong.magic.framework.common.enums.UserTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 刷新令牌
 */
@Data
@Accessors(chain = true)
public class SecurityRefreshTokenDTO {

    /**
     * 多租户编号
     */
    private Long tenantId;

    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 用户类型
     *
     * 枚举 {@link UserTypeEnum}
     */
    private Integer userType;
    /**
     * 客户端编号
     */
    private String clientId;

    /**
     * 过期时间
     */
    private LocalDateTime expiresTime;

}
