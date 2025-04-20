package cn.javayong.magic.framework.token.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.javayong.magic.framework.common.enums.UserTypeEnum;
import cn.javayong.magic.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.javayong.magic.framework.common.util.date.DateUtils;
import cn.javayong.magic.framework.common.util.json.JsonUtils;
import cn.javayong.magic.framework.token.core.dto.SecurityAccessTokenDTO;
import cn.javayong.magic.framework.token.core.dto.SecurityRefreshTokenDTO;
import cn.javayong.magic.framework.token.core.service.SecurityTokenService;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static cn.javayong.magic.framework.common.exception.util.ServiceExceptionUtil.exception0;
import static cn.javayong.magic.framework.common.util.collection.CollectionUtils.convertSet;

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
    @Override
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
        long timeDiff2 = LocalDateTimeUtil.between(LocalDateTime.now(), accessTokenDTO.getExpiresTime(), ChronoUnit.SECONDS);
        if (timeDiff2 > 0) {
            stringRedisTemplate.opsForValue().set(accessTokenKey, JsonUtils.toJsonString(accessTokenDTO), timeDiff2, TimeUnit.SECONDS);
        }

        return accessTokenDTO;
    }

    @Override
    public SecurityAccessTokenDTO refreshAccessToken(String refreshToken) {
        // step1:从 Redis 查看刷新令牌
        String refreshTokenKey = String.format(SECURITY_REFRESH_TOKEN, refreshToken);
        SecurityAccessTokenDTO refreshTokenDTO =
                JsonUtils.parseObject(
                        stringRedisTemplate.opsForValue().get(refreshTokenKey),
                        SecurityAccessTokenDTO.class);
        if (refreshTokenDTO == null) {
            throw exception0(GlobalErrorCodeConstants.BAD_REQUEST.getCode(), "无效的刷新令牌");
        }

        // step2:已过期的情况下，删除刷新令牌
        if (DateUtils.isExpired(refreshTokenDTO.getExpiresTime())) {
            stringRedisTemplate.delete(refreshTokenKey);
            throw exception0(GlobalErrorCodeConstants.UNAUTHORIZED.getCode(), "刷新令牌已过期");
        }

        // step3 ：创建 accessToken
        SecurityAccessTokenDTO accessTokenDTO = new SecurityAccessTokenDTO().setAccessToken(generateAccessToken())
                .setUserId(refreshTokenDTO.getUserId()).setUserType(refreshTokenDTO.getUserType())
                .setClientId(refreshTokenDTO.getClientId())
                .setRefreshToken(refreshTokenDTO.getRefreshToken())
                .setExpiresTime(LocalDateTime.now().plusSeconds(1800)).setTenantId(refreshTokenDTO.getTenantId());

        // step4 : 保存 accessToken 到 Redis
        String accessTokenKey = String.format(SECURITY_ACCESS_TOKEN, accessTokenDTO.getAccessToken());
        long timeDiff2 = LocalDateTimeUtil.between(LocalDateTime.now(), accessTokenDTO.getExpiresTime(), ChronoUnit.SECONDS);
        if (timeDiff2 > 0) {
            stringRedisTemplate.opsForValue().set(accessTokenKey, JsonUtils.toJsonString(accessTokenDTO), timeDiff2, TimeUnit.SECONDS);
        }

        return accessTokenDTO;
    }

    @Override
    public SecurityAccessTokenDTO removeAccessToken(String accessToken) {
        // step1:从 Redis 获取 访问令牌对象
        String accessTokenKey = String.format(SECURITY_ACCESS_TOKEN, accessToken);
        SecurityAccessTokenDTO securityAccessTokenDTO = JsonUtils.parseObject(
                stringRedisTemplate.opsForValue().get(accessTokenKey),
                SecurityAccessTokenDTO.class);
        if (securityAccessTokenDTO == null) {
            return null;
        }

        // step2:从 Redis 删除访问令牌
        stringRedisTemplate.delete(accessTokenKey);

        // step3:从 Redis 删除刷新令牌
        String refreshTokenKey = String.format(SECURITY_REFRESH_TOKEN, securityAccessTokenDTO.getRefreshToken());
        stringRedisTemplate.delete(refreshTokenKey);

        return securityAccessTokenDTO;
    }

    @Override
    public SecurityAccessTokenDTO getAccessToken(String accessToken) {
        String accessTokenKey = String.format(SECURITY_ACCESS_TOKEN, accessToken);
        SecurityAccessTokenDTO securityAccessTokenDTO = JsonUtils.parseObject(
                stringRedisTemplate.opsForValue().get(accessTokenKey),
                SecurityAccessTokenDTO.class);
        if (securityAccessTokenDTO == null) {
            return null;
        }
        return securityAccessTokenDTO;
    }

    @Override
    public SecurityAccessTokenDTO checkAccessToken(String accessToken) {
        SecurityAccessTokenDTO accessTokenDTO = getAccessToken(accessToken);
        if (accessTokenDTO == null) {
            throw exception0(GlobalErrorCodeConstants.UNAUTHORIZED.getCode(), "访问令牌不存在");
        }
        if (DateUtils.isExpired(accessTokenDTO.getExpiresTime())) {
            throw exception0(GlobalErrorCodeConstants.UNAUTHORIZED.getCode(), "访问令牌已过期");
        }
        return accessTokenDTO;
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
