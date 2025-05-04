package cn.javayong.magic.module.ai.adapter.command;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * OpenAI 聊天补全 API 响应实体
 */
@Data
public class OpenAIChatCompletions {

    /**
     * 本次对话的唯一标识ID
     */
    @JsonProperty("id")
    private String id;

    /**
     * 生成的候选结果列表（通常包含多个选择）
     */
    @JsonProperty("choices")
    private List<Choice> choices;

    /**
     * API 调用的token使用情况统计
     */
    @JsonProperty("usage")
    private Usage usage;

    /**
     * 对话创建时间戳（Unix时间戳格式）
     */
    @JsonProperty("created")
    private long created;

    /**
     * 使用的模型名称（如"gpt-3.5-turbo"）
     */
    @JsonProperty("model")
    private String model;

    /**
     * 对象类型（固定为"chat.completion"）
     */
    @JsonProperty("object")
    private String object;

    /**
     * 对话选项（包含单条生成结果）
     */
    @Data
    public static class Choice {
        /**
         * 生成的消息内容
         */
        @JsonProperty("message")
        private Message message;

        /**
         * 停止生成的原因（如"stop"表示正常结束）
         */
        @JsonProperty("finish_reason")
        private String finishReason;
    }

    /**
     * 消息内容实体
     */
    @Data
    public static class Message {
        /**
         * 消息角色（system/user/assistant/tool）
         */
        @JsonProperty("role")
        private String role;

        /**
         * 消息文本内容
         */
        @JsonProperty("content")
        private String content;

        /**
         * 推理过程内容（部分模型专用）
         */
        @JsonProperty("reasoning_content")
        private String reasoningContent;

        /**
         * 工具调用请求列表（函数调用场景使用）
         */
        @JsonProperty("tool_calls")
        private List<ToolCall> toolCalls;
    }

    /**
     * 工具调用请求
     */
    @Data
    public static class ToolCall {
        /**
         * 工具调用ID
         */
        @JsonProperty("id")
        private String id;

        /**
         * 工具类型（如"function"）
         */
        @JsonProperty("type")
        private String type;

        /**
         * 函数调用详情
         */
        @JsonProperty("function")
        private Function function;
    }

    /**
     * 函数调用参数
     */
    @Data
    public static class Function {
        /**
         * 函数名称
         */
        @JsonProperty("name")
        private String name;

        /**
         * 函数调用参数（JSON字符串格式）
         */
        @JsonProperty("arguments")
        private String arguments;
    }

    /**
     * Token用量统计
     */
    @Data
    public static class Usage {
        /**
         * 提示词消耗的token数量
         */
        @JsonProperty("prompt_tokens")
        private int promptTokens;

        /**
         * 生成内容消耗的token数量
         */
        @JsonProperty("completion_tokens")
        private int completionTokens;

        /**
         * 总消耗token数量
         */
        @JsonProperty("total_tokens")
        private int totalTokens;
    }
}
