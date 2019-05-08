package com.taobao.csp.sentinel.dashboard.interceptor;

import com.taobao.csp.sentinel.dashboard.annotation.Anonymous;
import com.taobao.csp.sentinel.dashboard.service.IAuthService;
import com.taobao.csp.sentinel.dashboard.util.CookieUtil;
import com.taobao.csp.sentinel.dashboard.util.jwt.IJwtInfo;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static com.taobao.csp.sentinel.dashboard.util.jwt.JwtConstants.ACCESS_TOKEN;

/**
 * 拦截方法执行，判断是否含有{@link ACCESS_TOKEN}
 * <p>
 * <note> 目标方法标注{@link @Anonymous}不进行拦截</note>
 *
 * @author jiashuai.xie
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {


    private static final Logger LOGGER = LoggerFactory.getLogger(TokenInterceptor.class);

    private final IAuthService authService;

    public TokenInterceptor(IAuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 匿名方法
        if (isAnonymous(handlerMethod)) {
            return true;
        }


        String token = CookieUtil.getCookieValue(request, ACCESS_TOKEN);

        boolean isAjax = CookieUtil.isAjax(request);

        // 没有登录
        if (StringUtils.isEmpty(token)) {
            if (isAjax) {
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().write("{\"code\":\"-1\",\"msg\":\"error\"}");
                return false;
            }
            // todo
            response.sendRedirect("/login.html");
            return false;
        }

        IJwtInfo jwtInfo = authService.validateToken(token);

        // 登录
        if (jwtInfo != null) {

            // 自动刷新
            Date expireDate = jwtInfo.getExpireDate();

            // 如果过期时间和当前时间相差1分钟 更新token
            if (new DateTime(expireDate.getTime()).minusMinutes(1).isBefore(DateTime.now())) {

                // 刷新token
                String refreshToken = authService.refreshToken(token);

                if (org.springframework.util.StringUtils.hasText(refreshToken)) {
                    response.setHeader("Set-Cookie", ACCESS_TOKEN + "=" + token + ";Path=/;Domain=aihuishou.com;HttpOnly;");
                } else {
                    // 刷新token 失败
                    LOGGER.error("failed to refresh token");
                }
            }

            return true;

        }

        if (isAjax) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("{\"code\":\"-1\",\"msg\":\"error\"}");
            return false;
        }
        // todo
        response.sendRedirect("/login.html");
        return false;
    }

    private boolean isAnonymous(HandlerMethod handlerMethod) {

        if (null != AnnotationUtils.findAnnotation(handlerMethod.getBean().getClass(), Anonymous.class)
                || null != AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Anonymous.class)) {
            return true;
        }

        return false;
    }


}
