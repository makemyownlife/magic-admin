package cn.javayong.magic.module.ai.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - AI 平台配置 Response VO")
@Data
public class AiPlatformRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2630")
    private Long id;

    @Schema(description = "模型名字", example = "张三")
    private String name;

    @Schema(description = "模型平台", example = "OpenAI")
    private String platform;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "状态", example = "2")
    private Integer status;

}