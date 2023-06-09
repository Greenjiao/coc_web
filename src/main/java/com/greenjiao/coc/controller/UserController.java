package com.greenjiao.coc.controller;

import com.greenjiao.coc.bean.User;
import com.greenjiao.coc.common.bean.CommonResult;
import com.greenjiao.coc.common.bean.LoginUser;
import com.greenjiao.coc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yan
 * @since 2023-06-09
 */
@RestController
@RequestMapping("/coc/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public CommonResult<LoginUser> login(@RequestBody User user){
        return userService.login(user);
    }
}
