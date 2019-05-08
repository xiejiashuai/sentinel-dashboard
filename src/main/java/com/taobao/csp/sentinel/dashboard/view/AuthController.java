
package com.taobao.csp.sentinel.dashboard.view;

import com.taobao.csp.sentinel.dashboard.annotation.Anonymous;
import com.taobao.csp.sentinel.dashboard.domain.UserLoginModel;
import com.taobao.csp.sentinel.dashboard.exception.ServiceAuthException;
import com.taobao.csp.sentinel.dashboard.service.IAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.taobao.csp.sentinel.dashboard.util.jwt.JwtConstants.ACCESS_TOKEN;

/**
 * 负责登录、校验
 *
 * @author jiashuai.xie
 */
@RestController
@RequestMapping(value = "/auth", consumes = "application/json")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @Anonymous
    @PostMapping("/login")
    public Result login(final @RequestBody @Valid UserLoginModel model, HttpServletResponse response) {


        Result result = new Result();

        try {

            String token = authService.login(model);

            response.setHeader("Set-Cookie", ACCESS_TOKEN + "=" + token + ";Path=/;Domain=aihuishou.com;HttpOnly;");

            result.setData("/index.htm");

            LOGGER.info("---->access-token:{}<----", token);

            return result;

        } catch (ServiceAuthException ex) {

            result.setData("/login.html");

            return result;

        }


    }


}
