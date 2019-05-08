package com.taobao.csp.sentinel.dashboard.service.impl;

import com.taobao.csp.sentinel.dashboard.domain.UserLoginModel;
import com.taobao.csp.sentinel.dashboard.exception.ServiceAuthException;
import com.taobao.csp.sentinel.dashboard.service.IAuthService;
import com.taobao.csp.sentinel.dashboard.util.jwt.IJwtInfo;
import com.taobao.csp.sentinel.dashboard.util.jwt.JwtInfo;
import com.taobao.csp.sentinel.dashboard.util.jwt.JwtTokenHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

/**
 * Implement {@link IAuthService}
 *
 * @author jiashuai.xie
 */
@Service
@Validated
public class AuthService implements IAuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    private static final String DEFAULT_USER_NAME = "sentinel";

    private static final String DEFAULT_PASSWORD = "sentinel123456";

    private static final String DEFAULT_USER_ID = "00001";

    @Override
    public String login(UserLoginModel loginModel) {

        if (!loginModel.getUserName().equals(DEFAULT_USER_NAME) || !loginModel.getPassword().equals(DEFAULT_PASSWORD)) {
            LOGGER.error("userName or password error");
            throw new ServiceAuthException("userName or password error");
        }

        // login success
        IJwtInfo jwtInfo = new JwtInfo(loginModel.getUserName(), DEFAULT_USER_ID, null);

        // 生成token
        return JwtTokenHelper.generateToken(jwtInfo);

    }

    @Override
    public IJwtInfo validateToken(@NotEmpty(message = "token can not be empty") String token) {

        try {

            IJwtInfo jwtInfo = JwtTokenHelper.parseToken(token);

            return jwtInfo;

        } catch (Exception e) {

            LOGGER.error("invalid token,must login");

            return null;
        }

    }

    @Override
    public String refreshToken(@NotEmpty(message = "Token can not be empty") String token) {


        try {

            String refreshToken = JwtTokenHelper.refreshToken(token);

            return refreshToken;

        } catch (Exception e) {

            LOGGER.error("invalid token,must login");

            return null;
        }
    }
}
