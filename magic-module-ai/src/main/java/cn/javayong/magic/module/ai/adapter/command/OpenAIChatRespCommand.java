package cn.javayong.magic.module.ai.adapter.command;

import lombok.Data;


@Data
public class OpenAIChatRespCommand<T> {

    public static Integer SUCCESS_CODE = 200;

    public static Integer INTERNAL_ERROR_CODE = 500;

    private Integer code;

    private String message;

    private T data;

}
