package cn.javayong.magic.framework.token.core.service;

import cn.javayong.magic.framework.token.core.dto.SecurityAccessTokenDTO;

public interface SecurityTokenService {

    SecurityAccessTokenDTO createAccessToken(Long userId, Long tenantId);

    SecurityAccessTokenDTO refreshAccessToken(String refreshToken);

    void removeAccessToken(String accessToken);

}
