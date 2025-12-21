package cn.javayong.magic.module.system.domain.vo;

import cn.javayong.magic.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.javayong.magic.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 系统客户端分页 Request VO")
@Data
public class SystemClientPageReqVO extends PageParam {

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

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}