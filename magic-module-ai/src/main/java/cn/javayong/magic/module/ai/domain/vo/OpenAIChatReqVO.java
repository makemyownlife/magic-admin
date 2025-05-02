package cn.javayong.magic.module.ai.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 标准 AI 对话格式
 */
@Data
public class OpenAIChatReqVO {

    private List<ChatMessage> messages;

    private boolean stream;

    private String model;

    private Double temperature;

    @JsonProperty("presence_penalty")
    private Double presencePenalty;

    @JsonProperty("frequency_penalty")
    private Double frequencyPenalty;

    @JsonProperty("top_p")
    private Double topP;

    @Data
    public static class ChatMessage {
        private String role; // "system", "user", "assistant"
        private String content;
    }

}
