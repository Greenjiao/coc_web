package com.greenjiao.coc.security.service;

import com.greenjiao.coc.bean.User;
import com.greenjiao.coc.common.constant.CacheConstant;
import com.greenjiao.coc.common.constant.CommonConstant;
import com.greenjiao.coc.common.exception.ServiceException;
import com.greenjiao.coc.manager.AsyncManager;
import com.greenjiao.coc.manager.factory.AsyncFactory;
import com.greenjiao.coc.security.utils.SecurityUtils;
import com.greenjiao.coc.utils.RedisUtils;
import com.greenjiao.coc.utils.sys.MessageUtils;
import org.apache.commons.lang3.ObjectUtils;
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
    private final RedisUtils redisUtils;

    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;

    public PasswordService(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }


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
        Integer retryCount = redisUtils.getCacheObject(getCacheKey(username));
        if (ObjectUtils.isEmpty(retryCount)) {
            retryCount = 0;
        }
        if (retryCount >= maxRetryCount) {
            String message = MessageUtils.message("user.password.retry.limit.exceed", maxRetryCount, lockTime);
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, CommonConstant.LOGIN_FAIL, message));
            redisUtils.setCacheObject(getCacheKey(username), retryCount, lockTime, TimeUnit.MINUTES);
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(), message);
        }
        if (!matches(user, password)) {
            retryCount = retryCount + 1;
            String message = MessageUtils.message("user.password.retry.limit.count", retryCount);
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