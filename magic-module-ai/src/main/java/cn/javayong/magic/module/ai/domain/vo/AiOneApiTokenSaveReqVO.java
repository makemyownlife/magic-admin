package cn.javayong.magic.module.ai.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - API oneapi token 新增/修改 Request VO")
@Data
public class AiOneApiTokenSaveReqVO {

    @Schema(description = "模型列表", example = "[1,2]")
    private String modelIds;

    @Schema(description = "令牌名称", example = "测试令牌")
    @NotEmpty(message = "令牌名称不能为空")
    private String name;

    @Schema(description = "令牌值 (实际API密钥)", example = "sk-1234567890abcdef")
    @NotEmpty(message = "令牌值不能为空")
    private String token;

    @Schema(description = "过期时间", example = "2025-12-31 23:59:59")
    @NotEmpty(message = "过期时间不能为空")
    private LocalDateTime expireTime;

}
