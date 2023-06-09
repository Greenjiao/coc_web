package com.greenjiao.coc.manager.factory;

import com.greenjiao.coc.bean.sys.SysLogininfor;
import com.greenjiao.coc.bean.sys.SysOperLog;
import com.greenjiao.coc.common.constant.CommonConstant;
import com.greenjiao.coc.service.sys.ISysLogininforService;
import com.greenjiao.coc.service.sys.ISysOperLogService;
import com.greenjiao.coc.utils.CocUtils;
import com.greenjiao.coc.utils.sys.*;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.TimerTask;


/**
 * @Author Yan
 * @Date 2023/06/05 17:17
 * @Version 1.0
 */
@Slf4j
public class AsyncFactory {
//    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息
     * @param args     列表
     * @return 任务task
     */
    public static TimerTask recordLogininfor(final String username, final String status, final String message,
                                             final Object... args) {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr();
        return new TimerTask() {
            @Override
            public void run() {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // 打印信息到日志
                log.info(s.toString(), args);
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                SysLogininfor logininfor = new SysLogininfor();
                logininfor.setUserName(username);
                logininfor.setIpaddr(ip);
                logininfor.setLoginLocation(address);
                logininfor.setBrowser(browser);
                logininfor.setOs(os);
                logininfor.setMsg(message);
                LocalDateTime nowLocalDateTime = CocUtils.getNowLocalDateTime();
                logininfor.setLoginTime(nowLocalDateTime);
                // 日志状态
                if (StringUtils.equalsAny(status, CommonConstant.LOGIN_SUCCESS, CommonConstant.LOGOUT, CommonConstant.REGISTER)) {
                    logininfor.setStatus(CommonConstant.SUCCESS);
                } else if (CommonConstant.LOGIN_FAIL.equals(status)) {
                    logininfor.setStatus(CommonConstant.FAIL);
                }
                // 插入数据
                SpringUtils.getBean(ISysLogininforService.class).insertLogininfor(logininfor);
            }
        };
    }

    /**
     * 操作日志记录
     *
     * @param operLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final SysOperLog operLog) {
        return new TimerTask() {
            @Override
            public void run() {
                // 远程查询操作地点
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(ISysOperLogService.class).insertOperlog(operLog);
            }
        };
    }
}
