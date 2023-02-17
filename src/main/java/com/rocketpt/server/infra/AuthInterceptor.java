package com.rocketpt.server.infra;

import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.SessionItemHolder;
import com.rocketpt.server.common.authz.PermissionHelper;
import com.rocketpt.server.common.authz.RequiresPermissions;
import com.rocketpt.server.common.exception.RocketPTException;
import com.rocketpt.server.sys.service.SessionService;
import com.rocketpt.server.dto.sys.UserinfoDTO;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author plexpt
 */

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    private final SessionService sessionService;

    public AuthInterceptor(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {
        if (request.getHeader(Constants.TOKEN_HEADER_NAME) == null) {
            log.warn("Request uri {} {} is unauthorized", request.getMethod(),
                    request.getRequestURI());
            throw new RocketPTException(CommonResultStatus.UNAUTHORIZED);
        }
        String token = request.getHeader(Constants.TOKEN_HEADER_NAME).replace("Bearer", "").trim();
        if (!sessionService.isLogin(token)) {
            throw new RocketPTException(CommonResultStatus.UNAUTHORIZED, "未登录");
        }
        UserinfoDTO loginUserInfo = sessionService.getLoginUserInfo(token);
        if (handler instanceof HandlerMethod handlerMethod) {
            RequiresPermissions requiresPermissions =
                    handlerMethod.getMethodAnnotation(RequiresPermissions.class);
            if (requiresPermissions != null) {
                if (!PermissionHelper.isPermitted(loginUserInfo.permissions(),
                        requiresPermissions.value(), requiresPermissions.logical())) {
                    throw new RocketPTException(CommonResultStatus.FORBIDDEN);
                }
            }
        }
        SessionItemHolder.setItem(Constants.SESSION_CURRENT_USER, loginUserInfo);
        return true;
    }
}
