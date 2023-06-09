package com.greenjiao.coc.common.constant;

/**
 * @Author Yan
 * @Date 2023/06/09 11:11
 * @Version 1.0
 */
public class CacheConstant {
    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "sys_coc:login_tokens:";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "sys_coc:captcha_codes:";
    /**
     * 登录账户密码错误次数 redis key
     */
    public static final String PWD_ERR_CNT_KEY = "sys_coc:pwd_err_cnt:";
//    public static final String redisTokenTable="sys_coc:token";
    public static final String TOKEN_BLACK_KEY="sys_coc:token_blacklist";
}
