package com.greenjiao.coc.service.sys.impl;

/**
 * @Author Yan
 * @Date 2023/06/06 14:36
 * @Version 1.0
 */

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.greenjiao.coc.bean.sys.SysLogininfor;
import com.greenjiao.coc.service.sys.ISysLogininforService;
import com.greenjiao.coc.service.sys.mapper.SysLogininforMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 系统访问日志情况信息 服务层处理
 *
 * @author ruoyi
 */
@Service
public class SysLogininforServiceImpl implements ISysLogininforService {

    @Autowired
    private SysLogininforMapper logininforMapper;

    /**
     * 新增系统登录日志
     *
     * @param logininfor 访问日志对象
     */
    @Override
    public void insertLogininfor(SysLogininfor logininfor) {
        logininforMapper.insert(logininfor);
//        logininforMapper.insertLogininfor(logininfor);
    }

    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    @Override
    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor) {
        LambdaQueryWrapper<SysLogininfor> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
                        SysLogininfor::getInfoId,
                        SysLogininfor::getOs,
                        SysLogininfor::getBrowser,
                        SysLogininfor::getMsg,
                        SysLogininfor::getIpaddr,
                        SysLogininfor::getStatus,
                        SysLogininfor::getLoginLocation,
                        SysLogininfor::getLoginTime,
                        SysLogininfor::getUserName)
                .eq(logininfor.getInfoId() != null, SysLogininfor::getInfoId, logininfor.getInfoId())
                .eq(logininfor.getOs() != null, SysLogininfor::getOs, logininfor.getOs())
                .eq(logininfor.getMsg() != null, SysLogininfor::getMsg, logininfor.getMsg())
                .eq(logininfor.getLoginLocation() != null, SysLogininfor::getLoginLocation, logininfor.getLoginLocation())
                .eq(logininfor.getLoginTime() != null, SysLogininfor::getLoginTime, logininfor.getLoginTime())
                .eq(logininfor.getBrowser() != null, SysLogininfor::getBrowser, logininfor.getBrowser())
                .eq(logininfor.getIpaddr() != null, SysLogininfor::getIpaddr, logininfor.getIpaddr())
                .eq(logininfor.getStatus() != null, SysLogininfor::getStatus, logininfor.getStatus())
                .eq(logininfor.getUserName() != null, SysLogininfor::getUserName, logininfor.getUserName());
        return logininforMapper.selectList(wrapper);
//        return logininforMapper.selectLogininforList(logininfor);
    }

    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    @Override
    public int deleteLogininforByIds(Long[] infoIds) {
        return logininforMapper.deleteBatchIds(Arrays.asList(infoIds));
//        return logininforMapper.deleteLogininforByIds(infoIds);
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLogininfor() {
        logininforMapper.cleanLogininfor();
    }
}
