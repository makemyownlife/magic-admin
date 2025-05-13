package cn.javayong.magic.module.ai.domain.vo;

import cn.javayong.magic.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - API 平台配置分页 Request VO")
@Data
public class AiPlatformPageReqVO extends PageParam {

    @Schema(description = "平台配置名称", example = "deepseek平台对话配置")
    private String name;

    @Schema(description = "绑定模型", example = "deepseek-chat")
    private String model;

    @Schema(description = "模型平台", example = "deepseek")
    private String platform;

}