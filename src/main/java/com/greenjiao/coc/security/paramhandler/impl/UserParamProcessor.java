package com.greenjiao.coc.security.paramhandler.impl;

import com.greenjiao.coc.bean.User;
import com.greenjiao.coc.common.enums.ResponseEnum;
import com.greenjiao.coc.common.exception.ServiceException;
import com.greenjiao.coc.mapper.UserMapper;
import com.greenjiao.coc.security.paramhandler.SecurityParamProcessor;
import org.apache.commons.lang3.ObjectUtils;

public class UserParamProcessor implements SecurityParamProcessor {
    private final UserMapper userMapper;

    public UserParamProcessor(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User getTargetUser(Object obj) {
        return (User) obj;
    }

    @Override
    public Object getTargetObj(String provideId, String methodName) {
        Object obj;
        obj = userMapper.selectById(provideId);
        if (ObjectUtils.isEmpty(obj)) {
            throw new ServiceException(ResponseEnum.NOT_FOUND.getCode(), "用户未找到");
        }
        return obj;
    }
}
