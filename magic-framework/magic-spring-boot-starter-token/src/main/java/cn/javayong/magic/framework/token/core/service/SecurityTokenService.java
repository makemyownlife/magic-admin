package cn.javayong.magic.framework.token.core.service;

import cn.javayong.magic.framework.token.core.dto.SecurityAccessTokenDTO;
import cn.javayong.magic.framework.token.core.dto.SecurityCreateTokenDTO;

public interface SecurityTokenService {

    SecurityAccessTokenDTO createAccessToken(SecurityCreateTokenDTO securityCreateTokenDTO);

    SecurityAccessTokenDTO refreshAccessToken(String refreshToken);

    SecurityAccessTokenDTO removeAccessToken(String accessToken);

    SecurityAccessTokenDTO getAccessToken(String accessToken);

    SecurityAccessTokenDTO checkAccessToken(String accessToken);

}
