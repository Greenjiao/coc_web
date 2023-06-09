package com.greenjiao.coc.security.service;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.greenjiao.coc.bean.Permission;
import com.greenjiao.coc.bean.association.UserPermission;
import com.greenjiao.coc.mapper.PermissionMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Yan
 * @Date 2023/06/09 12:33
 * @Version 1.0
 */
@Service("ss")
public class PermissionService {
    private final PermissionMapper permissionMapper;

    public PermissionService(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    public List<String> selectPermissionByUserId(String userId) {
        MPJLambdaWrapper<Permission> wrapper = new MPJLambdaWrapper<Permission>()
                .select(Permission::getName)
                .leftJoin(UserPermission.class, UserPermission::getPermissionId, Permission::getId)
                .eq(UserPermission::getUserId, userId);
        return permissionMapper.selectJoinList(String.class, wrapper);
    }
}
