package cn.javayong.magic.module.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 系统客户端 Response VO")
@Data
@ExcelIgnoreUnannotated
public class SystemClientRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16575")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "客户端KEY")
    @ExcelProperty("客户端KEY")
    private String clientKey;

    @Schema(description = "客户端密钥")
    @ExcelProperty("客户端密钥")
    private String clientSecret;

    @Schema(description = "授权类型", example = "2")
    @ExcelProperty("授权类型")
    private String grantType;

    @Schema(description = "设备类型", example = "2")
    @ExcelProperty("设备类型")
    private String deviceType;

    @Schema(description = "Token访问超时时间（秒）")
    @ExcelProperty("Token访问超时时间（秒）")
    private Integer accessTimeout;

    @Schema(description = "Token刷新超时时间（秒）")
    @ExcelProperty("Token刷新超时时间（秒）")
    private Integer refreshTimeout;

    @Schema(description = "状态（0 正常 1 停用）", example = "2")
    @ExcelProperty("状态（0 正常 1 停用）")
    private String status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}