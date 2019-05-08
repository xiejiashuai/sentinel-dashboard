package com.taobao.csp.sentinel.dashboard.util.jwt;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.*;
import org.joda.time.DateTime;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.taobao.csp.sentinel.dashboard.util.jwt.JwtConstants.*;

/**
 * a Helper to generate token by Jwt
 *
 * @author jiashuai.xie
 */
public class JwtTokenHelper {

    private static final byte[] bin = DatatypeConverter.parseBase64Binary
            ("f3973b64918e4324ad85acea1b6cbec5");


    private static Key generatorKey() {
        SignatureAlgorithm saa = SignatureAlgorithm.HS256;
        Key key = new SecretKeySpec(bin, saa.getJcaName());
        return key;
    }

    /**
     * @param jwtInfo user info
     * @return token
     */
    public static String generateToken(IJwtInfo jwtInfo) {

      return generateToken(jwtInfo,JWT_TOKEN_EXPIRE_TIME);

    }


    /**
     * @param jwtInfo user info
     * @param expire  过期时间 秒
     * @return token
     */
    public static String generateToken(IJwtInfo jwtInfo, int expire) {

        Date expireDate = DateTime.now().plusSeconds(expire).toDate();

        Map<String, Object> claims = new HashMap<>(16);
        claims.put(JWT_KEY_USER_ID, jwtInfo.getId());
        claims.put(JWT_KEY_NAME, jwtInfo.getUniqueName());
        claims.put(JWT_EXPIRE_NAME, expireDate);

        return Jwts.builder()
                // 设置token所属主体
                .setSubject(jwtInfo.getUniqueName())
                // 设置过期时间
                .setExpiration(expireDate)
                .setClaims(claims)
                // 设置加密算法
                .signWith(SignatureAlgorithm.HS256, generatorKey())
                // 返回简洁的URL-safe token
                .compact();


    }

    /**
     * 解析
     *
     * @param token 解析token
     * @return user info
     * @throws UnsupportedJwtException  if the {@code claimsJws} argument does not represent an Claims JWS
     * @throws MalformedJwtException    if the {@code claimsJws} string is not a valid JWS
     * @throws SignatureException       if the {@code claimsJws} JWS signature validation fails
     * @throws ExpiredJwtException      if the specified JWT is a Claims JWT and the Claims has an expiration time
     *                                  before the time this method is invoked.
     * @throws IllegalArgumentException if the {@code claimsJws} string is {@code null} or empty or only whitespace
     */
    public static IJwtInfo parseToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {

        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(generatorKey())
                .parseClaimsJws(token);

        Claims body = claimsJws.getBody();

        return new JwtInfo( JSON.toJSONString(body.get(JWT_KEY_NAME)), JSON.toJSONString(body.get(JWT_KEY_USER_ID)), JSON.parseObject(JSON.toJSONString(body.get(JWT_EXPIRE_NAME)),Date.class));

    }


    /**
     * 刷新token
     *
     * @param token
     * @return token
     * @throws UnsupportedJwtException  if the {@code claimsJws} argument does not represent an Claims JWS
     * @throws MalformedJwtException    if the {@code claimsJws} string is not a valid JWS
     * @throws SignatureException       if the {@code claimsJws} JWS signature validation fails
     * @throws ExpiredJwtException      if the specified JWT is a Claims JWT and the Claims has an expiration time
     *                                  before the time this method is invoked.
     * @throws IllegalArgumentException if the {@code claimsJws} string is {@code null} or empty or only whitespace
     */
    public static String refreshToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {

        IJwtInfo jwtInfo = parseToken(token);

        return generateToken(jwtInfo, JWT_TOKEN_EXPIRE_TIME);

    }


}
