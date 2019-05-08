package com.taobao.csp.sentinel.dashboard.service;

import com.taobao.csp.sentinel.dashboard.domain.UserLoginModel;
import com.taobao.csp.sentinel.dashboard.util.jwt.IJwtInfo;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

/**
 * 登录、校验服务
 *
 * @author jiashuai.xie
 */
@Validated
public interface IAuthService {

    /**
     * user login ,if success return true
     *
     * @param loginModel
     * @return token
     */
    String login(UserLoginModel loginModel);

    /**
     * 校验token 是否合法
     *
     * @param token
     * @return
     */
    IJwtInfo validateToken(@NotEmpty(message = "token can not be empty") String token);

    /**
     * 刷新token
     * @param token
     * @return token
     */
    String refreshToken(@NotEmpty(message = "Token can not be empty") String token);

}
