package com.greenjiao.coc.service.impl;

import com.greenjiao.coc.bean.Permission;
import com.greenjiao.coc.common.bean.CommonResult;
import com.greenjiao.coc.mapper.PermissionMapper;
import com.greenjiao.coc.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yan
 * @since 2023-06-09
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Override
    public CommonResult<Permission> selectAll() {
        return null;
    }

    @Override
    public CommonResult<Permission> insert(Permission permission) {
        return null;
    }

    @Override
    public CommonResult<Permission> deleteById(String id) {
        return null;
    }
}
