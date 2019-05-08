package com.taobao.csp.sentinel.dashboard.config;

import com.taobao.csp.sentinel.dashboard.annotation.Anonymous;
import com.taobao.csp.sentinel.dashboard.view.Result;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Global Error Handler
 *
 * @author jiashuai.xie
 */
@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
public class DefaultGlobalErrorController extends AbstractErrorController {

    private final ErrorProperties errorProperties;

    public DefaultGlobalErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties) {
        super(errorAttributes);
        this.errorProperties = serverProperties.getError();
    }

    @RequestMapping
    @Anonymous
    public Result error(HttpServletRequest request) {
        Result response = new Result();
        HttpStatus status = getStatus(request);
        response.setCode(status.value());
        Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
        response.setMsg(String.valueOf(body));
        return response;
    }

    /**
     * Determine if the stacktrace attribute should be included.
     *
     * @param request  the source request
     * @param produces the media type produced (or {@code MediaType.ALL})
     * @return if the stacktrace attribute should be included
     */
    protected boolean isIncludeStackTrace(HttpServletRequest request,
                                          MediaType produces) {
        ErrorProperties.IncludeStacktrace include = errorProperties.getIncludeStacktrace();
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        }
        if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
            return getTraceParameter(request);
        }
        return false;
    }

    @Override
    public String getErrorPath() {
        return errorProperties.getPath();
    }
}
