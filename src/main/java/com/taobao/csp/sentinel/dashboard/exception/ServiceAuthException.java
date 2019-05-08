package com.taobao.csp.sentinel.dashboard.exception;

/**
 * 服务校验异常
 *
 * @author jiashuai.xie
 */
public class ServiceAuthException extends RuntimeException {

    private Integer code;

    public ServiceAuthException() {

    }

    public ServiceAuthException(String msg) {
        super(msg);
    }

    public ServiceAuthException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

}
