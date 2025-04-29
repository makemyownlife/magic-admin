package cn.javayong.magic.framework.token.core.dto;

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



}
