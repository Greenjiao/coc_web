package com.greenjiao.coc.security.paramhandler;

import com.greenjiao.coc.bean.User;

public interface SecurityParamProcessor {
    /**
     * 获取操作目标对象关联的User,只能被关联的User操作
     *
     * @param obj 目标对象
     * @return 关联的用户
     */
    User getTargetUser(Object obj);

    Object getTargetObj(String provideId, String methodName);
}
