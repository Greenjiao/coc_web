package com.greenjiao.coc.mapper.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.greenjiao.coc.bean.User;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * @Author Yan
 * @Date 2023/06/09 12:23
 * @Version 1.0
 */
public class UserQuery {
    public static QueryWrapper<User> selectEntityQuery(User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("id", "account", "name", "gender", "telephone", "email", "signature", "avatar_address", "permission", "banned", "remark", "create_time")
                .eq(StringUtils.isNotBlank(user.getId()), "id", user.getId())
                .like(StringUtils.isNotBlank(user.getAccount()), "account", user.getAccount())
                .like(StringUtils.isNotBlank(user.getName()), "name", user.getName())
                .eq(StringUtils.isNotBlank(user.getGender()), "gender", user.getGender())
                .eq(StringUtils.isNotBlank(user.getTelephone()), "telephone", user.getTelephone())
                .eq(StringUtils.isNotBlank(user.getEmail()), "email", user.getEmail())
                .like(StringUtils.isNotBlank(user.getSignature()), "signature", user.getSignature())
                .eq(ObjectUtils.isNotEmpty(user.getPermission()), "permission", user.getPermission())
                .eq(ObjectUtils.isNotEmpty(user.getBanned()), "banned", user.getBanned())
                .like(StringUtils.isNotBlank(user.getRemark()), "remark", user.getRemark());
        return wrapper;
    }

    public static QueryWrapper<User> selectByIdQuery(String id, Boolean selectPwd) {
        // 设置selectPwd默认为false
        Boolean finalSelectPwd = Optional.ofNullable(selectPwd).orElse(false);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        ArrayList<String> fields = new ArrayList<>(Arrays.asList("id", "account", "name", "gender", "telephone", "email", "signature", "avatar_address", "permission", "banned", "remark", "create_time"));
        if (finalSelectPwd) {
            fields.add("password");
        }
        wrapper.select(fields)
                .eq("id", id);
        return wrapper;
    }

    public static QueryWrapper<User> selectByAccountQuery(String account, Boolean selectPwd) {
        // 设置selectPwd为可选参数并且默认为false
        Boolean finalSelectPwd = Optional.ofNullable(selectPwd).orElse(false);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        ArrayList<String> fields = new ArrayList<>(Arrays.asList("id", "account", "name", "gender", "telephone", "email", "signature", "avatar_address", "permission", "banned", "remark", "create_time"));
        if (finalSelectPwd) {
            fields.add("password");
        }
        wrapper.select(fields)
                .eq("account", account);
        return wrapper;
    }
}