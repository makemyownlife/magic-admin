package cn.javayong.magic.framework.token.core.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 安全客户端数据传输对象(DTO)
 * <p>
 * 用于在系统间传递客户端认证相关信息
 *
 * @author YourName
 * @date 2023-11-15
 */
@Data // Lombok 注解，自动生成 getter/setter、toString、equals 和 hashCode 方法
@Accessors(chain = true) // Lombok 注解，启用链式调用风格
public class SecurityClientDTO {

    /**
     * 客户端唯一标识ID
     * <p>
     * 用于唯一标识一个客户端实例
     */
    private String clientId;

    /**
     * 客户端密钥标识
     * <p>
     * 用于客户端认证的身份标识，相当于用户名
     * 注意：应与 clientSecret 配合使用
     */
    private String clientKey;

    /**
     * 客户端密钥
     * <p>
     * 用于客户端认证的凭证，相当于密码
     * 注意：应妥善保管，建议加密存储
     */
    private String clientSecret;

}
