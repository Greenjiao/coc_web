package com.greenjiao.coc.service;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.greenjiao.coc.bean.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.greenjiao.coc.common.bean.CommonResult;
import com.greenjiao.coc.mapper.PermissionMapper;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yan
 * @since 2023-06-09
 */

public interface PermissionService{
    CommonResult<Permission> selectAll();

    CommonResult<Permission> insert(Permission permission);

    CommonResult<Permission> deleteById(String id);
//    CommonResult<Permission> selectByUserId(String userId);
}
