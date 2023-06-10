package com.greenjiao.coc.controller;

import com.greenjiao.coc.bean.User;
import com.greenjiao.coc.common.bean.CommonResult;
import com.greenjiao.coc.common.bean.LoginUser;
import com.greenjiao.coc.common.bean.PageParam;
import com.greenjiao.coc.common.bean.PageResult;
import com.greenjiao.coc.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/register")
    public CommonResult<User> register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public CommonResult<LoginUser> login(@RequestBody User user) {
        return userService.login(user);
    }

    /**
     * 查询用户实体
     *
     * @param user      传入的要查询的用户信息
     * @param pageParam 分页信息
     * @return 分页数据
     */
    @PostMapping
    public PageResult<User> selectByEntity(@RequestBody User user, @Validated PageParam pageParam) {
        return userService.selectByEntity(user, pageParam.getPage(), pageParam.getLimit());
    }

    @PreAuthorize("@ss.hasPermi(@role.TOP_ADMIN) or #user.id==principal.user.id")
    @PutMapping
    public CommonResult<User> update(@RequestBody User user) {
        return userService.update(user);
    }
}
