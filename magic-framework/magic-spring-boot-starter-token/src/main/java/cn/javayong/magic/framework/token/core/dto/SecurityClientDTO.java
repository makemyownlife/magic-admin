package cn.javayong.magic.framework.token.core.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 客户端 DTO
 */
@Data
@Accessors(chain = true)
public class SecurityClientDTO {

    private String clientId;

    private String clientKey;

    private String clientSecret;

}
