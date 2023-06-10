package com.greenjiao.coc.security.config;

import com.greenjiao.coc.common.exception.GlobalExceptionHandler;
import com.greenjiao.coc.security.filter.JwtAuthenticationFilter;
import com.greenjiao.coc.security.service.UserDetailsServiceImpl;
import com.greenjiao.coc.security.utils.JwtUtils;
import com.greenjiao.coc.utils.RedisUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Author Yan
 * @Date 2023/06/09 8:23
 * @Version 1.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;
    private final SecurityProperties securityProperties;
    private final JwtUtils jwtUtils;
    private final RedisUtils redisUtils;
    private final GlobalExceptionHandler globalExceptionHandler;

    public SecurityConfig(GlobalExceptionHandler globalExceptionHandler, RedisUtils redisUtils, JwtUtils jwtUtils, SecurityProperties securityProperties, UserDetailsServiceImpl userDetailsService) {
        this.globalExceptionHandler = globalExceptionHandler;
        this.redisUtils = redisUtils;
        this.jwtUtils = jwtUtils;
        this.securityProperties = securityProperties;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // 提供自定义loadUserByUsername
        authProvider.setUserDetailsService(userDetailsService);
        // 指定密码编辑器
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //   不要注册成Bean对象，会被Spring容器识别自动注册到SpringBoot的拦截器链中
    public JwtAuthenticationFilter authenticationJwtTokenFilter() {
        return new JwtAuthenticationFilter(securityProperties, jwtUtils, redisUtils, globalExceptionHandler);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 开启跨域
                .cors().and()
                // CSRF 禁用，因为不使用 Session
                .csrf().disable()
                // 基于 token 机制，所以不需要 Session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/coc/user/login", "/coc/user/register").permitAll()
                .anyRequest().authenticated();
        return httpSecurity.build();
    }
}
