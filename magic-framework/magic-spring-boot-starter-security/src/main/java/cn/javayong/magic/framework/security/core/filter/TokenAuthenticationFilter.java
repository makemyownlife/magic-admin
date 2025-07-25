package cn.javayong.magic.framework.security.core.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.javayong.magic.framework.common.exception.ServiceException;
import cn.javayong.magic.framework.common.pojo.CommonResult;
import cn.javayong.magic.framework.common.util.servlet.ServletUtils;
import cn.javayong.magic.framework.security.config.SecurityProperties;
import cn.javayong.magic.framework.security.core.LoginUser;
import cn.javayong.magic.framework.security.core.util.SecurityFrameworkUtils;
import cn.javayong.magic.framework.token.core.dto.SecurityAccessTokenDTO;
import cn.javayong.magic.framework.token.core.service.SecurityTokenService;
import cn.javayong.magic.framework.web.core.handler.GlobalExceptionHandler;
import cn.javayong.magic.framework.web.core.util.WebFrameworkUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

/**
 * Token 过滤器，验证 token 的有效性
 * 验证通过后，获得 {@link LoginUser} 信息，并加入到 Spring Security 上下文
 */
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final SecurityProperties securityProperties;

    private final GlobalExceptionHandler globalExceptionHandler;

    private final SecurityTokenService securityTokenService;

    @Override
    @SuppressWarnings("NullableProblems")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // step1 : 从请求头获取 客户端 KEY
        String clientKey = WebFrameworkUtils.getRequest().getHeader(securityProperties.getClientKeyHeader());

        String token = SecurityFrameworkUtils.obtainAuthorization(request,
                securityProperties.getTokenHeader(), securityProperties.getTokenParameter());
        if (StrUtil.isNotEmpty(token)) {
            Integer userType = WebFrameworkUtils.getLoginUserType(request);
            try {
                // 1.1 基于 token 构建登录用户
                LoginUser loginUser = buildLoginUserByToken(token, userType);

                // 2. 设置当前用户
                if (loginUser != null) {
                    SecurityFrameworkUtils.setLoginUser(loginUser, request);
                }
            } catch (Throwable ex) {
                CommonResult<?> result = globalExceptionHandler.allExceptionHandler(request, ex);
                ServletUtils.writeJSON(response, result);
                return;
            }
        }

        // 继续过滤链
        chain.doFilter(request, response);
    }

    private LoginUser buildLoginUserByToken(String token, Integer userType) {
        try {
            SecurityAccessTokenDTO accessTokenDTO = securityTokenService.checkAccessToken(token);
            if (accessTokenDTO == null) {
                return null;
            }
            // 用户类型不匹配，无权限
            // 注意：只有 /api/* 和 /app-api/* 有 userType，才需要比对用户类型
            // 类似 WebSocket 的 /ws/* 连接地址，是不需要比对用户类型的
            if (userType != null
                    && ObjectUtil.notEqual(accessTokenDTO.getUserType(), userType)) {
                throw new AccessDeniedException("错误的用户类型");
            }
            // 构建登录用户
            return new LoginUser().setId(accessTokenDTO.getUserId()).setUserType(accessTokenDTO.getUserType())
                    .setInfo(new HashMap<>()) // 额外的用户信息
                    .setScopes(Collections.EMPTY_LIST)
                    .setExpiresTime(accessTokenDTO.getExpiresTime());
        } catch (ServiceException serviceException) {
            // 校验 Token 不通过时，考虑到一些接口是无需登录的，所以直接返回 null 即可
            return null;
        }
    }

    /**
     * 模拟登录用户，方便日常开发调试
     *
     * 注意，在线上环境下，一定要关闭该功能！！！
     *
     * @param request 请求
     * @param token 模拟的 token，格式为 {@link SecurityProperties#getMockSecret()} + 用户编号
     * @param userType 用户类型
     * @return 模拟的 LoginUser
     */
    private LoginUser mockLoginUser(HttpServletRequest request, String token, Integer userType) {
        if (!securityProperties.getMockEnable()) {
            return null;
        }
        // 必须以 mockSecret 开头
        if (!token.startsWith(securityProperties.getMockSecret())) {
            return null;
        }
        // 构建模拟用户
        Long userId = Long.valueOf(token.substring(securityProperties.getMockSecret().length()));
        return new LoginUser().setId(userId).setUserType(userType);
    }

}
