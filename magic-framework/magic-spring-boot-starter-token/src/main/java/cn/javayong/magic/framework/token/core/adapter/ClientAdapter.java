package cn.javayong.magic.framework.token.core.adapter;

import cn.javayong.magic.framework.token.core.dto.SecurityClientDTO;

/**
 * 客户端适配器
 */
public interface ClientAdapter {

    /**
     * 获取客户端信息
     */
    SecurityClientDTO getClient();

}
