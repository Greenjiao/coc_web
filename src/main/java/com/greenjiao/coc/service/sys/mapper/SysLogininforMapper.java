package com.greenjiao.coc.service.sys.mapper;

/**
 * @Author Yan
 * @Date 2023/06/06 14:40
 * @Version 1.0
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.greenjiao.coc.bean.sys.SysLogininfor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 系统访问日志情况信息 数据层
 *
 * @author ruoyi
 */
@Mapper
public interface SysLogininforMapper extends BaseMapper<SysLogininfor>
{
//    /**
//     * 新增系统登录日志
//     *
//     * @param logininfor 访问日志对象
//     */
//    public void insertLogininfor(SysLogininfor logininfor);
//
//    /**
//     * 查询系统登录日志集合
//     *
//     * @param logininfor 访问日志对象
//     * @return 登录记录集合
//     */
//    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor);
//
//    /**
//     * 批量删除系统登录日志
//     *
//     * @param infoIds 需要删除的登录日志ID
//     * @return 结果
//     */
//    public int deleteLogininforByIds(Long[] infoIds);
//
//    /**
//     * 清空系统登录日志
//     *
//     * @return 结果
//     */
    @Update("truncate table sys_logininfor")
    int cleanLogininfor();
}
