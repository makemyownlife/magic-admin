package cn.javayong.magic.framework.web.core.filter;

import cn.hutool.core.util.StrUtil;
import cn.javayong.magic.framework.web.config.WebProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * 过滤 /api/admin 或者 /api/app 等 API 请求的过滤器
 */
@RequiredArgsConstructor
public abstract class ApiRequestFilter extends OncePerRequestFilter {

    protected final WebProperties webProperties;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 只过滤 API 请求的地址
        String apiUri = request.getRequestURI().substring(request.getContextPath().length());
        return !StrUtil.startWithAny(apiUri, webProperties.getApi().getPrefix());
    }

}
