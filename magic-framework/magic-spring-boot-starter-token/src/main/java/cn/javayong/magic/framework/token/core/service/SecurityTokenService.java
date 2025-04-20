package cn.javayong.magic.framework.token.core.service;

import cn.javayong.magic.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.javayong.magic.framework.common.util.date.DateUtils;
import cn.javayong.magic.framework.token.core.dto.SecurityAccessTokenDTO;

import static cn.javayong.magic.framework.common.exception.util.ServiceExceptionUtil.exception0;

public interface SecurityTokenService {

    SecurityAccessTokenDTO createAccessToken(Long userId, Long tenantId);

    SecurityAccessTokenDTO refreshAccessToken(String refreshToken);

    SecurityAccessTokenDTO removeAccessToken(String accessToken);

    SecurityAccessTokenDTO getAccessToken(String accessToken);

    SecurityAccessTokenDTO checkAccessToken(String accessToken);

}
