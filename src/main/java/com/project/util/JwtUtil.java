package com.project.util;

import cn.hutool.core.lang.Assert;
import com.project.constants.RedisKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;

/**
 * JWT校验工具类
 * <ol>
 * <li>iss: jwt签发者</li>
 * <li>sub: jwt所面向的用户</li>
 * <li>aud: 接收jwt的一方</li>
 * <li>exp: jwt的过期时间，这个过期时间必须要大于签发时间</li>
 * <li>nbf: 定义在什么时间之前，该jwt都是不可用的</li>
 * <li>iat: jwt的签发时间</li>
 * <li>jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击</li>
 * </ol>
 */
@Log4j2
public class JwtUtil {
    /**
     * JWT 加解密类型
     */
    private static final SignatureAlgorithm JWT_ALG = SignatureAlgorithm.HS256;
    /**
     * JWT 生成密钥使用的密码
     */
    private static final String JWT_RULE = "zhang.hao111";

    /**
     * JWT 添加至HTTP HEAD中的前缀
     */
    private static final String JWT_SEPARATOR = "zhang ";

    /**
     * 使用JWT默认方式，生成加解密密钥
     *
     * @param alg 加解密类型
     * @return
     */
    public static SecretKey generateKey(SignatureAlgorithm alg) {
        return MacProvider.generateKey(alg);
    }

    /**
     * 使用指定密钥生成规则，生成JWT加解密密钥
     *
     * @param alg  加解密类型
     * @param rule 密钥生成规则
     * @return
     */
    public static SecretKey generateKey(SignatureAlgorithm alg, String rule) {
        // 将密钥生成键转换为字节数组
        byte[] bytes = Base64.decodeBase64(rule);
        // 根据指定的加密方式，生成密钥
        return new SecretKeySpec(bytes, alg.getJcaName());
    }

    /**
     * 构建JWT
     *
     * @param alg      jwt 加密算法
     * @param key      jwt 加密密钥
     * @param sub      jwt 面向的用户
     * @param aud      jwt 接收方
     * @param curtime      jwt 唯一身份标识
     * @param iss      jwt 签发者
     * @param nbf      jwt 生效日期时间
     * @param duration jwt 有效时间，单位：秒
     * @return JWT字符串
     */
    public static String buildJWT(SignatureAlgorithm alg, Key key, String sub, String aud, Map claims, String iss, Date nbf, Integer duration) {
        // jwt的签发时间
        Date iat = new Date();
        // jwt的过期时间，这个过期时间必须要大于签发时间
        Date exp = null;
        if (duration != null) {
            exp = DateUtils.addSeconds(nbf == null ? iat : nbf, duration);
        }
        // 获取JWT字符串
        String compact = Jwts.builder()
                .signWith(alg, key)
                .setAudience(aud)
                .setClaims(claims)
                .setSubject(sub)
                .setIssuer(iss)
                .setNotBefore(nbf)
//                .setIssuedAt(iat)
                .setExpiration(exp)
                .compact();

        // 在JWT字符串前添加"Bearer "字符串，用于加入"Authorization"请求头
        return JWT_SEPARATOR + compact;
    }


    /**
     * 构建JWT
     *
     * @param sub      jwt 面向的用户
     * @param aud      jwt 接收方
     * @param jti      jwt 唯一身份标识
     * @param iss      jwt 签发者
     * @param nbf      jwt 生效日期时间
     * @param duration jwt 有效时间，单位：秒
     * @return JWT字符串
     */
    public static String buildJWT(String sub, String aud, Map jti, String iss, Date nbf, Integer duration) {
        return buildJWT(JWT_ALG, generateKey(JWT_ALG, JWT_RULE), sub, aud, jti, iss, nbf, duration);
    }

    /**
     * 构建JWT
     *
     * @param sub jwt 面向的用户
     * @param curtime jwt 唯一身份标识，主要用来作为一次性token,从而回避重放攻击
     * @return JWT字符串
     */
    public static String buildJWT(String sub, String curtime, Integer duration) {
        Map<String,String> map = new HashMap<>();
        map.put(RedisKey.CURRENT_TIME_MILLIS,curtime);
        return buildJWT(sub, null, map, null, null, duration);
    }
    /**
     * 构建JWT
     *
     * @param sub jwt 面向的用户
     * @return JWT字符串
     */
    public static String buildJWT(String sub, Integer duration) {
        Map<String,String> map = new HashMap<>();
        map.put("sub",sub);
        return buildJWT(null, null, map, null, null, duration);
    }

    /**
     * 构建JWT
     * <p>使用 UUID 作为 jti 唯一身份标识</p>
     * <p>JWT有效时间 600 秒，即 10 分钟</p>
     *
     * @param sub jwt 面向的用户
     * @return JWT字符串
     */
    public static String buildJWT(String sub) {
        return buildJWT(sub, null, null, null, null, 3600);
    }

    /**
     * 解析JWT
     *
     * @param key       jwt 加密密钥
     * @param claimsJws jwt 内容文本
     * @return {@link Jws}
     * @throws Exception
     */
    public static Jws<Claims> parseJWT(Key key, String claimsJws) {
        // 移除 JWT 前的"Bearer "字符串
        claimsJws = StringUtils.substringAfter(claimsJws, JWT_SEPARATOR);
        // 解析 JWT 字符串
        return Jwts.parser().setSigningKey(key).parseClaimsJws(claimsJws);
    }

    /**
     * 校验JWT
     *
     * @param claimsJws jwt 内容文本
     * @return ture or false
     */
//    public static Boolean checkJWT(String claimsJws) {
//        boolean flag = false;
//        try {
//            SecretKey key = generateKey(JWT_ALG, JWT_RULE);
//            // 获取 JWT 的 payload 部分
//            flag = (parseJWT(key, claimsJws).getBody() != null);
//        } catch (Exception e) {
//            log.warn("JWT验证出错，错误原因：{}", e.getMessage());
//        }
//        return flag;
//    }

    /**
     * 校验JWT
     *
     * @param claimsJws jwt 内容文本
     * @return userName
     */
    public static Optional<String> getUserName(String claimsJws) {
        try {
            SecretKey key = generateKey(JWT_ALG, JWT_RULE);
            // 获取 JWT 的 payload 部分
            return Optional.ofNullable((String) parseJWT(key, claimsJws).getBody().get("sub"));
        } catch (Exception e) {
            log.warn("JWT验证出错，错误原因：{}", e.getMessage());
            return Optional.empty();
        }
    }
    /**
     * 校验JWT
     *
     * @param claimsJws jwt 内容文本
     * @return userName
     */
    public static Optional<String> getClaims(String claimsJws, String claimsName) {
        try {
            SecretKey key = generateKey(JWT_ALG, JWT_RULE);
            // 获取 JWT 的 payload 部分
             return Optional.of((String) parseJWT(key, claimsJws).getBody().get(claimsName));
        } catch (Exception e) {
            log.warn("JWT验证出错，错误原因：{}", e.getMessage());
        }
        return Optional.empty();
    }



}