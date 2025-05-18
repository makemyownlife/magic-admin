package cn.javayong.magic.module.ai.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - AI oneapi Token Response VO")
@Data
public class AiOneApiTokenRespVO {

    @Schema(description = "主键ID (雪花算法)", example = "1024")
    private Long id;

    @Schema(description = "模型列表", example = "[1,2]")
    private String modelIds;

    @Schema(description = "令牌名称", example = "测试令牌")
    private String name;

    @Schema(description = "令牌值 (实际API密钥)", example = "sk-1234567890abcdef")
    private String token;

    @Schema(description = "过期时间", example = "2025-12-31 23:59:59")
    private LocalDateTime expireTime;

    @Schema(description = "创建时间", example = "2023-01-01 10:00:00")
    private LocalDateTime createTime;

}