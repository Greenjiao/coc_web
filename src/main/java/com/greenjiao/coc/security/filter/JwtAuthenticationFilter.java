package com.greenjiao.coc.security.filter;

import com.greenjiao.coc.common.bean.LoginUser;
import com.greenjiao.coc.common.constant.CacheConstant;
import com.greenjiao.coc.common.exception.ServiceException;
import com.greenjiao.coc.security.config.SecurityProperties;
import com.greenjiao.coc.security.utils.JwtUtils;
import com.greenjiao.coc.utils.RedisUtils;
import com.greenjiao.coc.security.utils.SecurityUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * @Author Yan
 * @Date 2023/06/09 8:40
 * @Version 1.0
 */
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final SecurityProperties securityProperties;
    private final JwtUtils jwtUtils;
    private final RedisUtils redisUtils;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        List<String> permitAllUrls = securityProperties.getWhiteUrls();
        /* 白名单放行 */
        if(permitAllUrls.contains(requestURI)){
            filterChain.doFilter(request, response);
            return;
        }
        String token = SecurityUtils.obtainAuthorization(request, securityProperties.getTokenHeader());
        if(StringUtils.isNotEmpty(token)){
            if(jwtUtils.isTokenTampered(token)){
                throw new ServiceException(HttpStatus.UNAUTHORIZED.value(),"token验证不通过,请重新登陆");
            }
            String userId = jwtUtils.getUserId(token);
            String tokenKey = jwtUtils.getTokenKey(userId);
            LoginUser loginUser = redisUtils.getCacheObject(tokenKey);
            if(ObjectUtils.isEmpty(loginUser)){
                throw new ServiceException(HttpStatus.UNAUTHORIZED.value(),"token不存在,请重新登陆");
            }
            jwtUtils.verifyToken(loginUser);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }else{
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(),"未携带token");
        }
        filterChain.doFilter(request, response);
    }
}
