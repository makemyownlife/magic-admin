package cn.javayong.magic.module.ai.domain.vo;

import cn.javayong.magic.framework.common.enums.CommonStatusEnum;
import cn.javayong.magic.framework.common.validation.InEnum;
import cn.javayong.magic.module.ai.domain.enums.AiModelTypeEnum;
import cn.javayong.magic.module.ai.domain.enums.AiPlatformEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "管理后台 - API 平台新增/修改 Request VO")
@Data
public class AiPlatformSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "模型映射 JSON 格式", example = "[1,2,3]")
    private List<Integer> modelIds;

    @Schema(description = "平台配置名称", required = true, example = "OpenAI平台")
    @NotEmpty(message = "平台配置名称不能为空")
    private String name;

    @Schema(description = "api 请求 url", required = true, example = "https://api.openai.com/v1")
    @NotEmpty(message = "API请求URL不能为空")
    private String baseUrl;


    @Schema(description = "api 请求 key", required = true, example = "sk-xxxxxxxxxxxxxxxx")
    @NotEmpty(message = "api 请求 key不能为空")
    private String apiKey;

    @Schema(description = "平台类型，枚举：AiPlatformEnum", required = true, example = "OPEN_AI")
    @NotEmpty(message = "平台类型不能为空")
    private String platform;

    @Schema(description = "排序值", example = "1")
    private Integer sort;

    @Schema(description = "状态，枚举：CommonStatusEnum", required = true, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "模型映射列表", example = "")
    private List<AiPlatformSaveReqVO.ModelMapping> modelMappings;

    @Data
    public static class ModelMapping {

        private Long id;

        private String model;

        private String mappingName;

    }

}