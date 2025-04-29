package cn.javayong.magic;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.javayong.magic.framework.common.util.collection.SetUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static java.io.File.separator;

/**
 * 项目修改器，一键替换 Maven 的 groupId、artifactId，项目的 package 等
 * <p>
 * 通过修改 groupIdNew、artifactIdNew、projectBaseDirNew 三个变量
 *

 */
@Slf4j
public class ProjectReactor {

    private static final String GROUP_ID = "cn.javayong";
    private static final String ARTIFACT_ID = "magic";

    private static final String PACKAGE_NAME = "cn.javayong.magic";

    private static final String TITLE = "magic-admin管理系统";

    /**
     * 白名单文件，不进行重写，避免出问题
     */
    private static final Set<String> WHITE_FILE_TYPES = SetUtils.asSet("gif", "jpg", "svg", "png", // 图片
            "eot", "woff2", "ttf", "woff",  // 字体
            "xdb"); // IP 库

    public static void main(String[] args) {

    }

}
