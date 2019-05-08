package com.taobao.csp.sentinel.dashboard.domain;

/**
 * 用户登录
 *
 * @author jiashuai.xie
 */
public class UserLoginModel {

    private String userName;

    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
