package com.taobao.csp.sentinel.dashboard.util.jwt;

import com.taobao.csp.sentinel.dashboard.util.jwt.IJwtInfo;

import java.util.Date;

/**
 * Implement {@link IJwtInfo}
 *
 * @author jiashuai.xie
 */
public class JwtInfo implements IJwtInfo {

    private String uniqueName;

    private String id;

    private Date expireDate;

    public JwtInfo(String uniqueName, String id, Date expireDate) {
        this.uniqueName = uniqueName;
        this.id = id;
        this.expireDate = expireDate;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getUniqueName() {
        return uniqueName;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Date getExpireDate() {
        return expireDate;
    }
}
