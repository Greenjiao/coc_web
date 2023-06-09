package com.greenjiao.coc.controller;

import com.greenjiao.coc.bean.User;
import com.greenjiao.coc.common.bean.CommonResult;
import com.greenjiao.coc.common.bean.LoginUser;
import com.greenjiao.coc.common.bean.PageResult;
import com.greenjiao.coc.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yan
 * @since 2023-06-09
 */
@RestController
@RequestMapping("/coc/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public CommonResult<LoginUser> login(@RequestBody User user) {
        return userService.login(user);
    }
    @PreAuthorize("@ss.hasPermi('最高管理员') or #user.id == principal.user.id")
    @PostMapping
    public PageResult<User> selectByEntity(@RequestBody User user){
        return userService.selectByEntity(user,1,10);
    }
}
