package cn.javayong.magic.framework.token.core.service;

import cn.javayong.magic.framework.token.core.dto.SecurityAccessTokenDTO;
import cn.javayong.magic.framework.token.core.dto.SecurityCreateTokenDTO;

/**
 * 安全令牌核心服务接口
 * 职责：管理访问令牌（AccessToken）的全生命周期
 */
public interface SecurityTokenService {

    /**
     * 创建访问令牌
     * @param securityCreateTokenDTO 令牌创建参数（包含用户ID、客户端信息等）
     * @return 生成的令牌信息（含accessToken和refreshToken）
     */
    SecurityAccessTokenDTO createAccessToken(SecurityCreateTokenDTO securityCreateTokenDTO);

    /**
     * 刷新访问令牌
     * @param refreshToken 有效的刷新令牌
     * @return 新的令牌信息（含新accessToken和新refreshToken）
     */
    SecurityAccessTokenDTO refreshAccessToken(String refreshToken);

    /**
     * 主动使令牌失效（如用户登出时调用）
     * @param accessToken 需要失效的令牌
     * @return 被移除的令牌信息（含失效前的元数据）
     */
    SecurityAccessTokenDTO removeAccessToken(String accessToken);

    /**
     * 获取令牌详细信息（不验证有效性）
     * @param accessToken 待查询的令牌
     * @return 令牌完整信息（含用户ID、过期时间等）
     */
    SecurityAccessTokenDTO getAccessToken(String accessToken);

    /**
     * 验证令牌有效性（含黑名单检查）
     * @param accessToken 待验证的令牌
     * @return 令牌信息
     */
    SecurityAccessTokenDTO checkAccessToken(String accessToken);

}