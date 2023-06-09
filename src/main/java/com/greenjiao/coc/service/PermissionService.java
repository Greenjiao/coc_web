package com.greenjiao.coc.service;

import com.greenjiao.coc.bean.Permission;
import com.greenjiao.coc.common.bean.CommonResult;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yan
 * @since 2023-06-09
 */

public interface PermissionService {
    CommonResult<Permission> selectAll();

    CommonResult<Permission> insert(Permission permission);

    CommonResult<Permission> deleteById(String id);
//    CommonResult<Permission> selectByUserId(String userId);
}
