package cn.javayong.magic.module.ai.adapter.command;

import lombok.Data;


@Data
public class OpenAIChatRespCommand<T> {

    private Integer code;

    private String message;

    private T data;

}
