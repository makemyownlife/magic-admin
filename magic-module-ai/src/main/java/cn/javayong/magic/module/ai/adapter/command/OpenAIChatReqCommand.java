package cn.javayong.magic.module.ai.adapter.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OpenAIChatReqCommand {

    private List<OpenAIChatReqCommand.ChatMessage> messages;

    private boolean stream;

    private String model;

    private Double temperature = 0.7;

    @JsonProperty("presence_penalty")
    private Double presencePenalty;

    @JsonProperty("top_p")
    private Double topP;

    @Data
    public static class ChatMessage {

        private String role; // "system", "user", "assistant"
        private String content;

        public ChatMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

}
