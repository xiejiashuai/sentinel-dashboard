package com.taobao.csp.sentinel.dashboard.util.jwt;

import java.util.Date;

/**
 * store user info
 *
 * @author jiashuai.xie
 */
public interface IJwtInfo {

    /**
     * get user unique name
     *
     * @return user name
     */
    String getUniqueName();

    /**
     * get user id
     *
     * @return user id
     */
    String getId();

    /**
     * 获取过期时间
     * @return
     */
    Date getExpireDate();


}
