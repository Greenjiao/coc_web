package com.greenjiao.coc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.greenjiao.coc.bean.User;
import com.greenjiao.coc.common.bean.CommonResult;
import com.greenjiao.coc.common.bean.LoginUser;
import com.greenjiao.coc.common.exception.ServiceException;
import com.greenjiao.coc.mapper.UserMapper;
import com.greenjiao.coc.mapper.query.UserQuery;
import com.greenjiao.coc.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yan
 * @since 2023-06-09
 */
@Transactional
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserMapper userMapper, AuthenticationManager authenticationManager) {
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public CommonResult<User> register(User user) {
        return null;
    }

    @Override
    public CommonResult<LoginUser> login(User user) {
        Authentication authentication=null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getAccount(), user.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (InternalAuthenticationServiceException e){
            Throwable cause = e.getCause();
            if(cause instanceof ServiceException){
                throw (ServiceException) cause;
            }
        } finally {
            SecurityContextHolder.clearContext();
        }
        LoginUser loginUser = (LoginUser)authentication.getPrincipal();
        loginUser.setUser(loginUser.getUser().setPassword(null));

        return CommonResult.success(loginUser,"登陆成功");
    }

    @Override
    public CommonResult<User> update(User user) {
        return null;
    }

    @Override
    public CommonResult<User> logicDeleteById(String id) {
        return null;
    }

    @Override
    public CommonResult<User> selectByEntity(User user, Integer page, Integer limit) {
        return null;
    }

    @Override
    public CommonResult<User> selectByToken(String token) {
        return null;
    }

    @Override
    public User selectUserByUserName(String id) {
        QueryWrapper<User> userQueryWrapper = UserQuery.selectByAccountQuery(id, true);
        return userMapper.selectOne(userQueryWrapper);
    }
}
