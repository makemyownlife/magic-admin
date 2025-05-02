package cn.javayong.magic.module.ai.adapter.command;

import cn.javayong.magic.module.ai.domain.vo.OpenAIChatReqVO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OpenAIChatReqCommand {

    private List<OpenAIChatReqCommand.ChatMessage> messages;

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
