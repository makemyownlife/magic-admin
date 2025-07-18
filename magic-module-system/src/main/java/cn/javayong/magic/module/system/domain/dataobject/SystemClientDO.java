package cn.javayong.magic.module.system.domain.dataobject;

import cn.javayong.magic.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 系统客户端 DO
 *
 * @author admin
 */
@TableName("system_client")
@KeySequence("system_client_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemClientDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 客户端KEY
     */
    private String clientKey;
    /**
     * 客户端密钥
     */
    private String clientSecret;
    /**
     * 授权类型
     */
    private String grantType;
    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * Token访问超时时间（秒）
     */
    private Integer accessTimeout;
    /**
     * Token刷新超时时间（秒）
     */
    private Integer refreshTimeout;
    /**
     * 状态（0 正常 1 停用）
     */
    private String status;


}