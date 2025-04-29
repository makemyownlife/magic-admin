package cn.javayong.magic.framework.token.core.dto;

import cn.javayong.magic.framework.common.enums.UserTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SecurityCreateTokenDTO {

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 多租户编号
     */
    private Long tenantId;

    /**
     * 用户类型
     * <p>
     * 枚举 {@link UserTypeEnum}
     */
    private Integer userType;


}
