package cn.javayong.magic.module.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.*;

@Data
@Schema(description = "管理后台 - 系统客户端新增/修改 Request VO")
public class SystemClientSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16575")
    private Long id;

    @Schema(description = "客户端KEY")
    private String clientKey;

    @Schema(description = "客户端密钥")
    private String clientSecret;

    @Schema(description = "授权类型", example = "2")
    private String grantType;

    @NotNull(message = "设备类型不能为空")
    @Schema(description = "设备类型", example = "0")
    private Integer deviceType;

    @NotNull(message = "Token访问超时时间不能为空")
    @Min(value = 1800, message = "Token访问超时时间必须大于等于半个小时")
    @Schema(description = "Token访问超时时间（秒）")
    private Integer accessTimeout;

    @NotNull(message = "Token刷新超时时间不能为空")
    @Min(value = 3600, message = "Token刷新超时时间必须大于等于半个小时")
    @Schema(description = "Token刷新超时时间（秒）")
    private Integer refreshTimeout;

    @NotNull(message = "状态不能为空")
    @Schema(description = "状态（0 正常 1 停用）", example = "2")
    private Integer status;
}
