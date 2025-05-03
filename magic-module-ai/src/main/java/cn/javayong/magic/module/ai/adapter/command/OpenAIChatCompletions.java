package cn.javayong.magic.module.ai.adapter.command;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@Data
public class OpenAIChatCompletions {

    @JsonProperty("id")
    private String id;

    @JsonProperty("choices")
    private List<Choice> choices;

    @JsonProperty("usage")
    private Usage usage;

    @JsonProperty("created")
    private long created;

    @JsonProperty("model")
    private String model;

    @JsonProperty("object")
    private String object;

    @Data
    public static class Choice {
        @JsonProperty("message")
        private Message message;
        @JsonProperty("finish_reason")
        private String finishReason;
    }

    @Data
    public static class Message {

        @JsonProperty("role")
        private String role;

        @JsonProperty("content")
        private String content;

        @JsonProperty("reasoning_content")
        private String reasoningContent;

        @JsonProperty("tool_calls")
        private List<ToolCall> toolCalls;

    }

    @Data
    public static class ToolCall {

        @JsonProperty("id")
        private String id;

        @JsonProperty("type")
        private String type;

        @JsonProperty("function")
        private Function function;

    }

    @Data
    public static class Function {

        @JsonProperty("name")
        private String name;

        @JsonProperty("arguments")
        private String arguments;

    }

    @Data
    public static class Usage {

        @JsonProperty("prompt_tokens")
        private int promptTokens;

        @JsonProperty("completion_tokens")
        private int completionTokens;

        @JsonProperty("total_tokens")
        private int totalTokens;

    }

}
