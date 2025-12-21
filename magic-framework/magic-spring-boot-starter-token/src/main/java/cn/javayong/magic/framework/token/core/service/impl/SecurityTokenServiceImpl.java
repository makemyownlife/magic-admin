package cn.javayong.magic.framework.token.core.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import cn.javayong.magic.framework.client.core.adapter.ClientAdapter;
import cn.javayong.magic.framework.client.core.dto.SecurityClientDTO;
import cn.javayong.magic.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.javayong.magic.framework.common.util.date.DateUtils;
import cn.javayong.magic.framework.common.util.json.JsonUtils;
import cn.javayong.magic.framework.token.core.dto.SecurityAccessTokenDTO;
import cn.javayong.magic.framework.token.core.dto.SecurityCreateTokenDTO;
import cn.javayong.magic.framework.token.core.dto.SecurityRefreshTokenDTO;
import cn.javayong.magic.framework.token.core.service.SecurityTokenService;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static cn.javayong.magic.framework.common.exception.util.ServiceExceptionUtil.exception0;

public class SecurityTokenServiceImpl implements SecurityTokenService {

    /**
     * 访问令牌的Redis Key模板
     * 格式化参数说明：
     * 第一个参数: 应用名称 (appName)
     * 第二个参数: 令牌类型 (固定为"security_access_token")
     * 第三个参数: 客户端标识 (clientId)
     * 第四个参数: 令牌值 (tokenValue)
     * 最终格式: {appName}:security_access_token:{clientId}:{tokenValue}
     * 示例: "myapp:security_access_token:client123:abc123"
     */
    private static final String SECURITY_ACCESS_TOKEN = "%s:security_access_token:%s:%s";

    /**
     * 刷新令牌的Redis Key模板
     * 格式化参数说明：
     * 第一个参数: 应用名称 (appName)
     * 第二个参数: 令牌类型 (固定为"security_refresh_token")
     * 第三个参数: 客户端标识 (clientId)
     * 第四个参数: 令牌值 (tokenValue)
     * 最终格式: {appName}:security_refresh_token:{clientId}:{tokenValue}
     * 示例: "myapp:security_refresh_token:client123:xyz456"
     */
    private static final String SECURITY_REFRESH_TOKEN = "%s:security_refresh_token:%s:%s";

    private final StringRedisTemplate stringRedisTemplate;

    private final ClientAdapter clientAdapter;

    public SecurityTokenServiceImpl(ClientAdapter clientAdapter, StringRedisTemplate stringRedisTemplate) {
        this.clientAdapter = clientAdapter;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    //=========================================== 核心方法 start ===================================================

    @Override
    public SecurityAccessTokenDTO createAccessToken(SecurityCreateTokenDTO securityCreateTokenDTO) {

        // step1 ：获取 客户端信息
        SecurityClientDTO securityClientDTO = clientAdapter.getClient();

        // step2 ：创建 refreshToken
        SecurityRefreshTokenDTO refreshTokenDTO = new SecurityRefreshTokenDTO().setRefreshToken(generateRefreshToken()).setUserId(securityCreateTokenDTO.getUserId()).setUserType(securityCreateTokenDTO.getUserType()).setClientId(securityClientDTO.getClientId()).setExpiresTime(LocalDateTime.now().plusSeconds(securityClientDTO.getRefreshTimeout()));

        // step3 : 保存 refreshToken 到 Redis
        String refreshTokenKey = String.format(SECURITY_REFRESH_TOKEN, securityClientDTO.getNamespace(), securityClientDTO.getClientId(), refreshTokenDTO.getRefreshToken());
        long timeDiff1 = LocalDateTimeUtil.between(LocalDateTime.now(), refreshTokenDTO.getExpiresTime(), ChronoUnit.SECONDS);
        if (timeDiff1 > 0) {
            stringRedisTemplate.opsForValue().set(refreshTokenKey, JsonUtils.toJsonString(refreshTokenDTO), timeDiff1, TimeUnit.SECONDS);
        }

        // step4 ：创建 accessToken
        SecurityAccessTokenDTO accessTokenDTO = new SecurityAccessTokenDTO().setAccessToken(generateAccessToken()).setUserId(refreshTokenDTO.getUserId()).setUserType(refreshTokenDTO.getUserType()).setClientId(refreshTokenDTO.getClientId()).setRefreshToken(refreshTokenDTO.getRefreshToken()).setExpiresTime(LocalDateTime.now().plusSeconds(securityClientDTO.getAccessTimeout()));

        // step5 : 保存 accessToken 到 Redis
        String accessTokenKey = String.format(SECURITY_ACCESS_TOKEN, securityClientDTO.getNamespace(), securityClientDTO.getClientId(), accessTokenDTO.getAccessToken());
        long timeDiff2 = LocalDateTimeUtil.between(LocalDateTime.now(), accessTokenDTO.getExpiresTime(), ChronoUnit.SECONDS);
        if (timeDiff2 > 0) {
            stringRedisTemplate.opsForValue().set(accessTokenKey, JsonUtils.toJsonString(accessTokenDTO), timeDiff2, TimeUnit.SECONDS);
        }

        return accessTokenDTO;
    }

    @Override
    public SecurityAccessTokenDTO refreshAccessToken(String refreshToken) {

        // step1 ：获取 客户端信息
        SecurityClientDTO securityClientDTO = clientAdapter.getClient();

        // step2:从 Redis 查看刷新令牌
        String refreshTokenKey = String.format(SECURITY_REFRESH_TOKEN, securityClientDTO.getNamespace(), securityClientDTO.getClientId(), refreshToken);
        SecurityAccessTokenDTO refreshTokenDTO = JsonUtils.parseObject(stringRedisTemplate.opsForValue().get(refreshTokenKey), SecurityAccessTokenDTO.class);
        if (refreshTokenDTO == null) {
            throw exception0(GlobalErrorCodeConstants.BAD_REQUEST.getCode(), "无效的刷新令牌");
        }

        // step3:已过期的情况下，删除刷新令牌
        if (DateUtils.isExpired(refreshTokenDTO.getExpiresTime())) {
            stringRedisTemplate.delete(refreshTokenKey);
            throw exception0(GlobalErrorCodeConstants.UNAUTHORIZED.getCode(), "刷新令牌已过期");
        }

        // step4: 创建 accessToken
        SecurityCreateTokenDTO securityCreateTokenDTO = new SecurityCreateTokenDTO();
        securityCreateTokenDTO.setUserType(refreshTokenDTO.getUserType())
                .setUserId(refreshTokenDTO.getUserId());

        return createAccessToken(securityCreateTokenDTO);
    }

    @Override
    public SecurityAccessTokenDTO removeAccessToken(String accessToken) {

        // step1:获取 客户端信息
        SecurityClientDTO securityClientDTO = clientAdapter.getClient();

        // step2:从 Redis 获取 访问令牌对象
        String accessTokenKey = String.format(SECURITY_ACCESS_TOKEN, securityClientDTO.getNamespace(), securityClientDTO.getClientId(), accessToken);
        SecurityAccessTokenDTO securityAccessTokenDTO = JsonUtils.parseObject(stringRedisTemplate.opsForValue().get(accessTokenKey), SecurityAccessTokenDTO.class);
        if (securityAccessTokenDTO == null) {
            return null;
        }

        // step2:从 Redis 删除访问令牌
        stringRedisTemplate.delete(accessTokenKey);

        // step4:从 Redis 删除刷新令牌
        String refreshTokenKey = String.format(SECURITY_REFRESH_TOKEN, securityClientDTO.getNamespace(), securityClientDTO.getClientId(), securityAccessTokenDTO.getRefreshToken());
        stringRedisTemplate.delete(refreshTokenKey);

        return securityAccessTokenDTO;
    }

    @Override
    public SecurityAccessTokenDTO getAccessToken(String accessToken) {
        SecurityClientDTO securityClientDTO = clientAdapter.getClient();

        String accessTokenKey = String.format(SECURITY_ACCESS_TOKEN, securityClientDTO.getNamespace(), securityClientDTO.getClientId(), accessToken);

        SecurityAccessTokenDTO securityAccessTokenDTO = JsonUtils.parseObject(stringRedisTemplate.opsForValue().get(accessTokenKey), SecurityAccessTokenDTO.class);

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
