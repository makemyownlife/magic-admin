package cn.javayong.magic.module.infra.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static cn.hutool.core.util.ArrayUtil.firstMatch;

/**
 * 代码生成的场景枚举
 *
 * 
 */
@AllArgsConstructor
@Getter
public enum CodegenSceneEnum {

    ADMIN(1, "管理后台", "admin", ""),
    APP(2, "用户 APP", "app", "App");

    /**
     * 场景
     */
    private final Integer scene;
    /**
     * 场景名
     */
    private final String name;
    /**
     * 基础包名
     */
    private final String basePackage;
    /**
     * Controller 和 VO 类的前缀
     */
    private final String prefixClass;

    public static CodegenSceneEnum valueOf(Integer scene) {
        return firstMatch(sceneEnum -> sceneEnum.getScene().equals(scene), values());
    }

}
