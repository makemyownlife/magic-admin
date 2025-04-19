package cn.javayong.magic.framework.common.util.monitor;

import cn.hutool.core.lang.UUID;

/**
 * 链路追踪工具类
 *
 * 考虑到每个 starter 都需要用到该工具类，所以放到 common 模块下的 util 包下
 *

 */
public class TracerUtils {

    /**
     * 私有化构造方法
     */
    private TracerUtils() {
    }

    /**
     * 获得链路追踪编号。
     * 如果不存在的话为空字符串！！！
     *
     * @return 链路追踪编号
     */
    public static String getTraceId() {
        return UUID.randomUUID().toString();
    }

}
