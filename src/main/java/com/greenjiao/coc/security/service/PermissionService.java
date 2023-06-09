package com.greenjiao.coc.security.service;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.greenjiao.coc.bean.Permission;
import com.greenjiao.coc.bean.association.UserPermission;
import com.greenjiao.coc.common.bean.LoginUser;
import com.greenjiao.coc.mapper.PermissionMapper;
import com.greenjiao.coc.security.utils.SecurityUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * @Author Yan
 * @Date 2023/06/09 12:33
 * @Version 1.0
 */
@Service("ss")
public class PermissionService {
    /** 所有权限标识 */
    private static final String ALL_PERMISSION = "*:*:*";

    /** 管理员角色权限标识 */
    private static final String SUPER_ADMIN = "admin";

    private static final String ROLE_DELIMETER = ",";

    private static final String PERMISSION_DELIMETER = ",";
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

    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPermi(String permission)
    {
        if (StringUtils.isEmpty(permission))
        {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (ObjectUtils.isEmpty(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions()))
        {
            return false;
        }
        return hasPermissions(loginUser.getPermissions(), permission);
    }
    /**
     * 判断是否包含权限
     *
     * @param permissions 权限列表
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    private boolean hasPermissions(List<String> permissions, String permission)
    {
        return permissions.contains(ALL_PERMISSION) || permissions.contains(StringUtils.trim(permission));
    }
}
