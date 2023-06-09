package com.greenjiao.coc.security.config;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * @Author Yan
 * @Date 2023/06/09 9:04
 * @Version 1.0
 */
@Data
@Validated
@ConfigurationProperties(prefix = "coc.security")
public class SecurityProperties {
    /**
     * HTTP 请求时，访问令牌的请求 Header
     */
    @NotEmpty(message = "Token Header 不能为空")
    private String tokenHeader = "Authorization";
    private String bearerPrefix = "Bearer ";
    private String tokenIssuer = "greenjiao.coc.yan";
    private String tokenAudience = "greenjiao.coc.app";
    /**
     * 免登录的 URL 列表
     */
    private List<String> whiteUrls = Collections.emptyList();

    /**
     * PasswordEncoder 加密复杂度，越高开销越大
     */
    private Integer passwordEncoderLength = 4;
}
