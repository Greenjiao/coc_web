package com.greenjiao.coc.common.enums;

import com.greenjiao.coc.bean.Permission;
import com.greenjiao.coc.bean.User;
import org.springframework.stereotype.Component;

/**
 * @Author Yan
 * @Date 2023/06/10 10:10
 * @Version 1.0
 */
@Component("clazz")
public class ClazzEnum {
    public static final Class<User> userClass = User.class;
    public static final Class<Permission> permissionClass = Permission.class;
}
