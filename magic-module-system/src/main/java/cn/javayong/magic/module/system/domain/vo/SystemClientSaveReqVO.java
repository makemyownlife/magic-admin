package cn.javayong.magic.module.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 系统客户端新增/修改 Request VO")
@Data
public class SystemClientSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16575")
    private Long id;

    @Schema(description = "客户端KEY")
    private String clientKey;

    @Schema(description = "客户端密钥")
    private String clientSecret;

    @Schema(description = "授权类型", example = "2")
    private String grantType;

    @Schema(description = "设备类型", example = "2")
    private String deviceType;

    @Schema(description = "Token访问超时时间（秒）")
    private Integer accessTimeout;

    @Schema(description = "Token刷新超时时间（秒）")
    private Integer refreshTimeout;

    @Schema(description = "状态（0 正常 1 停用）", example = "2")
    private String status;

}