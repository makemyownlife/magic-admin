package cn.javayong.magic.module.infra.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 代码生成的前端类型枚举
 *

 */
@AllArgsConstructor
@Getter
public enum CodegenFrontTypeEnum {

    VUE2(10), // Vue2 Element UI 标准模版
    VUE3(20), // Vue3 Element Plus 标准模版
    VUE3_VBEN(30), // Vue3 VBEN 模版
    ;

    /**
     * 类型
     */
    private final Integer type;

}
