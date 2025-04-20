package cn.javayong.magic.framework.token.core.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import cn.javayong.magic.framework.common.enums.UserTypeEnum;
import cn.javayong.magic.framework.common.util.json.JsonUtils;
import cn.javayong.magic.framework.token.core.dto.SecurityAccessTokenDTO;
import cn.javayong.magic.framework.token.core.dto.SecurityRefreshTokenDTO;
import cn.javayong.magic.framework.token.core.service.SecurityTokenService;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class SecurityTokenServiceImpl implements SecurityTokenService {

    // 访问令牌
    private final static String SECURITY_ACCESS_TOKEN = "security_access_token:%s";

    // 刷新令牌
    private final static String SECURITY_REFRESH_TOKEN = "security_refresh_token:%s";

    private StringRedisTemplate stringRedisTemplate;

    public SecurityTokenServiceImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    //=========================================== 核心方法 start ===================================================
    public SecurityAccessTokenDTO createAccessToken(Long userId, Long tenantId) {
        // step1 ：创建 refreshToken
        SecurityRefreshTokenDTO refreshTokenDTO = new SecurityRefreshTokenDTO().setRefreshToken(generateRefreshToken())
                .setUserId(userId).setUserType(UserTypeEnum.ADMIN.getValue())
                .setClientId("default")
                .setExpiresTime(LocalDateTime.now().plusSeconds(2592000)).setTenantId(tenantId);

        // step2 : 保存 refreshToken 到 Redis
        String refreshTokenKey = String.format(SECURITY_REFRESH_TOKEN, refreshTokenDTO.getRefreshToken());
        long timeDiff1 = LocalDateTimeUtil.between(LocalDateTime.now(), refreshTokenDTO.getExpiresTime(), ChronoUnit.SECONDS);
        if (timeDiff1 > 0) {
            stringRedisTemplate.opsForValue().set(refreshTokenKey, JsonUtils.toJsonString(refreshTokenDTO), timeDiff1, TimeUnit.SECONDS);
        }

        // step3 ：创建 accessToken
        SecurityAccessTokenDTO accessTokenDTO = new SecurityAccessTokenDTO().setAccessToken(generateAccessToken())
                .setUserId(refreshTokenDTO.getUserId()).setUserType(refreshTokenDTO.getUserType())
                .setClientId(refreshTokenDTO.getClientId())
                .setRefreshToken(refreshTokenDTO.getRefreshToken())
                .setExpiresTime(LocalDateTime.now().plusSeconds(1800)).setTenantId(tenantId);

        // step4 : 保存 accessToken 到 Redis
        String accessTokenKey = String.format(SECURITY_ACCESS_TOKEN, accessTokenDTO.getAccessToken());
        long timeDiff2 = LocalDateTimeUtil.between(LocalDateTime.now(), refreshTokenDTO.getExpiresTime(), ChronoUnit.SECONDS);
        if (timeDiff2 > 0) {
            stringRedisTemplate.opsForValue().set(accessTokenKey, JsonUtils.toJsonString(accessTokenDTO), timeDiff2, TimeUnit.SECONDS);
        }

        return accessTokenDTO;
    }

    public SecurityAccessTokenDTO refreshAccessToken(String refreshToken) {
        return null;
    }

    public void removeAccessToken(String accessToken) {

    }

    //=========================================== 核心方法 end  ===========================================


    //=========================================== 辅助方法 start ===========================================
    private static String generateAccessToken() {
        return IdUtil.fastSimpleUUID();
    }

    private static String generateRefreshToken() {
        return IdUtil.fastSimpleUUID();
    }
    //=========================================== 辅助方法 end  ===========================================

}
