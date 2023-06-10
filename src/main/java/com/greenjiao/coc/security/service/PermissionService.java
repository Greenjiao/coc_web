package com.greenjiao.coc.security.service;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.greenjiao.coc.bean.Permission;
import com.greenjiao.coc.bean.User;
import com.greenjiao.coc.bean.association.UserPermission;
import com.greenjiao.coc.common.bean.LoginUser;
import com.greenjiao.coc.mapper.PermissionMapper;
import com.greenjiao.coc.mapper.UserMapper;
import com.greenjiao.coc.security.paramhandler.SecurityParamProcessor;
import com.greenjiao.coc.security.paramhandler.impl.UserParamProcessor;
import com.greenjiao.coc.security.utils.SecurityUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author Yan
 * @Date 2023/06/09 12:33
 * @Version 1.0
 */
@Service("ss")
public class PermissionService {
    /**
     * 所有权限标识
     */
    private static final String ALL_PERMISSION = "*:*:*";
    private final PermissionMapper permissionMapper;

    private final Map<Class<?>, SecurityParamProcessor> processorMap = new HashMap<>();

    public PermissionService(PermissionMapper permissionMapper, UserMapper userMapper) {
        this.permissionMapper = permissionMapper;
        processorMap.put(User.class, new UserParamProcessor(userMapper));
    }

    /**
     * 根据UserId获取其对应的权限
     *
     * @param userId 用户id
     * @return 权限列表
     */
    public List<String> selectPermissionByUserId(String userId) {
        MPJLambdaWrapper<Permission> wrapper = new MPJLambdaWrapper<Permission>()
                .select(Permission::getName)
                .leftJoin(UserPermission.class, UserPermission::getPermissionId, Permission::getId)
                .eq(UserPermission::getUserId, userId);
        return permissionMapper.selectJoinList(String.class, wrapper);
    }

//    /**
//     * 限定用户只能操作和自己关联的数据
//     * @param targetObj
//     * 目标对象
//     * @param clazz
//     * 目标对象类型   (适配多种类型，通过参数处理器不同的实现类)
//     */
//    public boolean limit(Object targetObj, Class<?> clazz){
//        LoginUser loginUser = SecurityUtils.getLoginUser();
//        if (ObjectUtils.isEmpty(loginUser)) {
//            throw new ServiceException(ResponseEnum.UNAUTHORIZED);
//        }
//        SecurityParamProcessor processor = processorMap.get(clazz);
//        User targetUser = processor.getTargetUser(targetObj);
//        if (ObjectUtils.isNotEmpty(targetUser)) {
//            return targetUser.getId().equals(loginUser.getUser().getId());
//        } else {
//            throw new ServiceException(ResponseEnum.NOT_FOUND.getCode(), "目标对象不存在");
//        }
//    }

    /**
     * 验证用户缺少某权限
     *
     * @param permission 权限
     * @return 用户是否缺少某权限
     */
    public boolean lackPermi(Permission permission) {
        return !hasPermi(permission);
    }

    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限
     * @return 用户是否具备某权限
     */
    public boolean hasPermi(Permission permission) {
        String roleName = permission.getName();
        if (StringUtils.isEmpty(roleName)) {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (ObjectUtils.isEmpty(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        return hasPermission(loginUser.getPermissions(), roleName);
    }

    /**
     * 验证用户是否具有以下任意一个权限
     *
     * @param permissions 权限列表
     * @return 用户是否具有以下任意一个权限
     */
    public boolean hasAnyPermi(Permission... permissions) {
        if (ObjectUtils.isEmpty(permissions)) {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (ObjectUtils.isEmpty(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        for (Permission permission : permissions) {
            if (hasPermi(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否包含权限
     *
     * @param roleNames 权限名称列表
     * @param roleName  权限名称字符串
     * @return 用户是否具备某权限
     */
    private boolean hasPermission(List<String> roleNames, String roleName) {
        return roleNames.contains(ALL_PERMISSION) || roleNames.contains(StringUtils.trim(roleName));
    }
}
