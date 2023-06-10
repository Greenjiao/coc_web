package com.greenjiao.coc.common.enums;

import com.greenjiao.coc.bean.Permission;
import org.springframework.stereotype.Component;

/**
 * @Author Yan
 * @Date 2023/06/10 9:01
 * @Version 1.0
 */
@Component("role")
public class RoleEnum {
    public static final Permission BANNED_USER = new Permission("1", 0, "封禁用户");
    public static final Permission BLOCKED_SPEECH_USER = new Permission("2", 10, "禁言用户");
    public static final Permission NORMAL_USER = new Permission("3", 20, "普通用户");
    public static final Permission PREMIUM_USER = new Permission("4", 30, "高级用户");
    public static final Permission NORMAL_ADMIN = new Permission("5", 100, "管理员");
    public static final Permission TOP_ADMIN = new Permission("6", 999, "最高管理员");
}
