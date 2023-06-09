package com.greenjiao.coc.service;

import com.greenjiao.coc.bean.User;
import com.greenjiao.coc.common.bean.CommonResult;
import com.greenjiao.coc.common.bean.LoginUser;
import com.greenjiao.coc.common.bean.PageResult;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yan
 * @since 2023-06-09
 */
public interface UserService {
    CommonResult<User> register(User user);

    CommonResult<LoginUser> login(User user);

    CommonResult<User> update(User user);

    CommonResult<User> logicDeleteById(String id);

    PageResult<User> selectByEntity(User user, Integer page, Integer limit);

    CommonResult<User> selectByToken(String token);

    User selectUserByUserName(String id);

}
