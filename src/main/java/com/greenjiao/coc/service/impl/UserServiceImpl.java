package com.greenjiao.coc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.greenjiao.coc.bean.User;
import com.greenjiao.coc.bean.association.UserPermission;
import com.greenjiao.coc.common.bean.CommonResult;
import com.greenjiao.coc.common.bean.LoginUser;
import com.greenjiao.coc.common.bean.PageResult;
import com.greenjiao.coc.common.constant.CommonConstant;
import com.greenjiao.coc.common.enums.ResponseEnum;
import com.greenjiao.coc.common.enums.RoleEnum;
import com.greenjiao.coc.common.exception.ServiceException;
import com.greenjiao.coc.manager.AsyncManager;
import com.greenjiao.coc.manager.factory.AsyncFactory;
import com.greenjiao.coc.mapper.UserMapper;
import com.greenjiao.coc.mapper.UserPermissionMapper;
import com.greenjiao.coc.mapper.query.UserQuery;
import com.greenjiao.coc.service.UserService;
import com.greenjiao.coc.utils.CocUtils;
import com.greenjiao.coc.utils.sys.MessageUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
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
    private final UserPermissionMapper userPermissionMapper;

    public UserServiceImpl(UserMapper userMapper, AuthenticationManager authenticationManager, UserPermissionMapper userPermissionMapper) {
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.userPermissionMapper = userPermissionMapper;
    }

    @Override
    public CommonResult<User> register(User user) {
        // 根据账号查询
        QueryWrapper<User> selectByAccountQuery = UserQuery.selectByAccountQuery(user.getAccount(), false);
        User u = userMapper.selectOne(selectByAccountQuery);
        if (ObjectUtils.isNotEmpty(u)) {
            String message = MessageUtils.message("user.account.exist");
            throw new ServiceException(ResponseEnum.BAD_REQUEST.getCode(), message);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        LocalDateTime nowLocalDateTime = CocUtils.getNowLocalDateTime();

        // 设置 ID,密文,日期
        String id = IdWorker.getIdStr();
        user.setId(id);
        user.setPassword(encodedPassword);
        user.setCreateTime(nowLocalDateTime);

        // 写入用户表
        userMapper.insert(user);

        // 写入权限中间表
        UserPermission userPermission = new UserPermission(id, RoleEnum.NORMAL_USER.getId());
        userPermissionMapper.insert(userPermission);
        String message = MessageUtils.message("user.register.success");

        return CommonResult.success(null, message);
    }

    @Override
    public CommonResult<LoginUser> login(User user) {
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getAccount(), user.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (InternalAuthenticationServiceException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ServiceException) {
                throw (ServiceException) cause;
            }
        } finally {
            SecurityContextHolder.clearContext();
        }
        if (ObjectUtils.isEmpty(authentication)) {
            throw new ServiceException(ResponseEnum.UNAUTHORIZED.getCode(), "未获取到认证信息,登陆失败");
        }
        // 敏感信息置空返回
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        loginUser.setUser(loginUser.getUser().setPassword(null));

        String message = MessageUtils.message("user.login.success");
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(user.getAccount(), CommonConstant.LOGIN_SUCCESS, message));
        return CommonResult.success(loginUser, message);
    }

    @Override
    public CommonResult<User> update(User user) {
        // 如果携带密码,则说明修改了密码,对新密码加密
        if (StringUtils.isNotBlank(user.getPassword())) {
            // BCrypt 加密
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
        }

        // 数据库操作
        userMapper.updateById(user);

        // 按 ID 查询,去除 pwd 字段
        QueryWrapper<User> selectByIdQuery = UserQuery.selectByIdQuery(user.getId(), false);
        User u = userMapper.selectOne(selectByIdQuery);

        return CommonResult.success(u, ResponseEnum.UPDATE_SUCCESS.getMsg());
    }

    @Override
    public CommonResult<User> logicDeleteById(String id) {
        return null;
    }

    @Override
    public PageResult<User> selectByEntity(User user, Integer page, Integer limit) {
        Page<User> userPage = new Page<>(page, limit);
        QueryWrapper<User> selectEntityQuery = UserQuery.selectEntityQuery(user);
        Page<User> userIPage = userMapper.selectPage(userPage, selectEntityQuery);
        return new PageResult<>(userIPage.getRecords(), userIPage.getTotal());
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
