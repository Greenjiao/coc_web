package com.greenjiao.coc.security.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.greenjiao.coc.bean.User;
import com.greenjiao.coc.common.bean.LoginUser;
import com.greenjiao.coc.common.constant.CommonConstant;
import com.greenjiao.coc.common.exception.ServiceException;
import com.greenjiao.coc.manager.AsyncManager;
import com.greenjiao.coc.manager.factory.AsyncFactory;
import com.greenjiao.coc.mapper.UserMapper;
import com.greenjiao.coc.mapper.query.UserQuery;
import com.greenjiao.coc.security.utils.JwtUtils;
import com.greenjiao.coc.utils.CocUtils;
import com.greenjiao.coc.utils.RedisUtils;
import com.greenjiao.coc.utils.sys.MessageUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Author Yan
 * @Date 2023/06/09 11:39
 * @Version 1.0
 */
@Slf4j
@Service
@Data
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserMapper userMapper;
    private final PasswordService passwordService;
    private final PermissionService permissionService;
    private final JwtUtils jwtUtils;
    private final RedisUtils redisUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> userQueryWrapper = UserQuery.selectByAccountQuery(username, true);
        User user = userMapper.selectOne(userQueryWrapper);

        if (ObjectUtils.isEmpty(user)) {
            String message = MessageUtils.message("user.account.not.exist", username);
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, CommonConstant.LOGIN_FAIL, message));
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(), message);
        } else if (user.getBanned()) {
            String message = MessageUtils.message("user.blocked", username);
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, CommonConstant.LOGIN_FAIL, message));
            throw new ServiceException(HttpStatus.FORBIDDEN.value(), message);
        }

        passwordService.validate(user);
        Integer randomExpire = getRandomExpire();
        UserDetails loginUser = createLoginUser(user);

        redisUtils.setCacheObject(jwtUtils.getTokenKey(user.getId()), loginUser, randomExpire, TimeUnit.MINUTES);
        return loginUser;
    }

    private Integer getRandomExpire() {
        Random random = new Random();
        return random.nextInt(21) + 30;
    }

    public UserDetails createLoginUser(User user) {
        String token = jwtUtils.createToken(user.getId());
        LocalDateTime nowLocalDateTime = CocUtils.getNowLocalDateTime();
        LocalDateTime expireLocalDateTime = nowLocalDateTime.plusMinutes(30L);
        List<String> permissions = permissionService.selectPermissionByUserId(user.getId());
        return new LoginUser(user, expireLocalDateTime, nowLocalDateTime, token, permissions);
    }
}
