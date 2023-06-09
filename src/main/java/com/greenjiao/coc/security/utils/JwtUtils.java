package com.greenjiao.coc.security.utils;

import com.greenjiao.coc.common.bean.LoginUser;
import com.greenjiao.coc.common.constant.CacheConstant;
import com.greenjiao.coc.security.config.SecurityProperties;
import com.greenjiao.coc.utils.CocUtils;
import com.greenjiao.coc.utils.RedisUtils;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author Yan
 * @Date 2023/06/09 9:38
 * @Version 1.0
 */
@Component
@AllArgsConstructor
public class JwtUtils {
    /**
     * jwt的唯一标识
     */
    public static final String JWT_ID = UUID.randomUUID().toString().replace("-", "");
    /**
     * 过期时间10日
     */
    private static final long EXPIRE_TIME = 10 * 24 * 60 * 60 * 1000L;
    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;
    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;
    private static final int TOKEN_EXPIRE_TIME = 30;
    /**
     * 加密密钥
     */
    private static final String KEY = "SecretCodeByYanQQ2662334834HelloWorldGoodLuckToYou";
    private final SecurityProperties securityProperties;
    private final RedisUtils redisUtils;

    /**
     * 生成token
     */
    public String createToken(String id) {
        return createToken(securityProperties.getTokenIssuer(), securityProperties.getTokenAudience(), id);
    }

    public String createToken(String issuer, String audience, String id) {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        //setID:设置jwtId
        //setExpiration:token过期时间  当前时间+有效时间
        //setSubject:设置主题为用户id
        //setIssuedAt:token创建时间
        //signWith:加密方式
        JwtBuilder builder = Jwts.builder()
                .setHeader(header)
                .setId(JWT_ID)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .setSubject(id)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, KEY)
                .setIssuer(issuer)
                .setAudience(audience);
        return builder.compact();
    }

    // 因为过滤器是在ApplicationContext前面加载的，获取不到IOC容器里面的bean，可以用这种方法获取
    public <T> T getBean(Class<T> clazz, HttpServletRequest request) {
        WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        return applicationContext.getBean(clazz);
    }

    /**
     * 验证token是否过期
     *
     * @param token 请求头中携带的token
     * @return token验证结果
     */
    public boolean isTokenExpired(String token) {

        Claims claims = Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token).getBody();
        Date expiration = claims.getExpiration();
        Date currentDate = new Date();

        // 判断token过期
        return expiration.before(currentDate);
    }

    /**
     * token是否被篡改
     */
    public boolean isTokenTampered(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);
            return false; // 没有抛出异常，说明JWT token没有被篡改
        } catch (JwtException | IllegalArgumentException e) {
            // JwtException异常包含了多种可能的异常情况，例如签名无效、过期、格式错误等,说明JWT token已经被篡改
            return true;
        }
    }

    /**
     * 指定token过期时间为当前，即立即失效
     */
    public String updateTokenExpiration(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token).getBody();
        claims.setExpiration(new Date());
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, KEY).compact();
    }

    /**
     * 将指定的JWT令牌加入到Redis Set中
     */
    public void addTokenToBlacklist(String token) {
        redisUtils.sSet(CacheConstant.TOKEN_BLACK_KEY, token);
    }

    /**
     * 检查指定的JWT令牌是否在Redis Set中
     */
    public boolean isTokenInBlacklist(String token) {
        return redisUtils.sHasKey(CacheConstant.TOKEN_BLACK_KEY, token);
    }

    /**
     * 3、解析token字符串中的权限信息
     *
     * @param token
     * @return
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                // 获取alg开头的信息
                .setSigningKey(KEY)
                .build()
                // 解析token字符串
                .parseClaimsJws(token)
                .getBody();
    }

    public String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader(securityProperties.getTokenHeader());
        return authHeader.substring(securityProperties.getBearerPrefix().length());
    }

    public String getUserId(String token) {
        Claims body = Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token).getBody();
        return body.getSubject();
    }

    public String getTokenKey(String userId) {
        return CacheConstant.LOGIN_TOKEN_KEY + userId;
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
    public void verifyToken(LoginUser loginUser) {
        LocalDateTime expireLocalDateTime = loginUser.getExpireTime();
        long expireTime = expireLocalDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        LocalDateTime nowLocalDateTime = CocUtils.getNowLocalDateTime();
        LocalDateTime expireLocalDateTime = nowLocalDateTime.plusMinutes(TOKEN_EXPIRE_TIME);
        loginUser.setLoginTime(nowLocalDateTime);
        loginUser.setExpireTime(expireLocalDateTime);
        // 根据id将loginUser缓存
        String userKey = getTokenKey(loginUser.getUser().getId());
        redisUtils.setCacheObject(userKey, loginUser, TOKEN_EXPIRE_TIME, TimeUnit.MINUTES);
    }
}
