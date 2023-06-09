package com.greenjiao.coc.security.service;

import com.greenjiao.coc.bean.User;
import com.greenjiao.coc.common.constant.CacheConstant;
import com.greenjiao.coc.common.constant.CommonConstant;
import com.greenjiao.coc.common.exception.ServiceException;
import com.greenjiao.coc.manager.AsyncManager;
import com.greenjiao.coc.manager.factory.AsyncFactory;
import com.greenjiao.coc.security.utils.SecurityUtils;
import com.greenjiao.coc.utils.RedisUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author Yan
 * @Date 2023/06/09 11:40
 * @Version 1.0
 */
@Component
public class PasswordService {
    @Autowired
    private RedisUtils redisUtils;

    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;


    /**
     * 登录账户密码错误次数缓存键名
     *
     * @param username 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username) {
        return CacheConstant.PWD_ERR_CNT_KEY + username;
    }

    public void validate(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String password = authentication.getCredentials().toString();
        String username = authentication.getName();
        Object cacheObject = redisUtils.getCacheObject(getCacheKey(username));
        Integer retryCount = null;
        if (ObjectUtils.isEmpty(cacheObject)) {
            retryCount = 0;
        } else {
            retryCount = (Integer) cacheObject;
        }
        if (retryCount >= maxRetryCount) {
            String message = "密码输入错误" + maxRetryCount + "次，帐户锁定" + lockTime + "分钟";
            System.out.println("输出了" + message);
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, CommonConstant.LOGIN_FAIL, message));
            redisUtils.setCacheObject(getCacheKey(username), String.valueOf(retryCount), lockTime, TimeUnit.MINUTES);
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(), message);
        }
        if (!matches(user, password)) {
            retryCount = retryCount + 1;
            String message = "密码输入错误 " + retryCount + " 次";
            System.out.println("输出了" + message);
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, CommonConstant.LOGIN_FAIL, message));
            redisUtils.setCacheObject(getCacheKey(username), retryCount, lockTime, TimeUnit.MINUTES);
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(), message);
        } else {
            clearLoginRecordCache(username);
        }
    }

    public boolean matches(User user, String rawPassword) {
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    public void clearLoginRecordCache(String loginName) {
        if (redisUtils.hasKey(getCacheKey(loginName))) {
            redisUtils.deleteObject(getCacheKey(loginName));
        }
    }
}