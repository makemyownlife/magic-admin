package cn.javayong.magic.module.ai.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 标准 AI 对话格式
 */
@Data
@Schema(description = "OpenAI 聊天请求参数")
public class OpenAIChatReqVO {

    @Schema(description = "消息列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<ChatMessage> messages;

    @Schema(description = "是否流式输出", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private boolean stream;

    @Schema(description = "模型名称", example = "gpt-3.5-turbo")
    private String model;

    @Schema(description = "采样温度，0-2，越高越随机", example = "1.0")
    private Double temperature = 0.7;

    @JsonProperty("presence_penalty")
    @Schema(description = "存在惩罚，-2.0到2.0，越高越避免新话题", example = "0.0")
    private Double presencePenalty = 0.0;

    @JsonProperty("top_p")
    @Schema(description = "核心采样，0-1，与temperature二选一", example = "1.0")
    private Double topP = 1.0;

    @Data
    @Schema(description = "聊天消息")
    public static class ChatMessage {
        @Schema(description = "角色: system/user/assistant", requiredMode = Schema.RequiredMode.REQUIRED, example = "user")
        private String role; // "system", "user", "assistant"

        @Schema(description = "消息内容", requiredMode = Schema.RequiredMode.REQUIRED)
        private String content;
    }

}
