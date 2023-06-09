package com.greenjiao.coc.common.enums;

/**
 * @Author Yan
 * @Date 2023/06/08 23:59
 * @Version 1.0
 */
public interface ResponseCodeEnum {
    Response SUCCESS = new Response(200, "成功");

    // ========== 客户端错误段 ==========

    Response BAD_REQUEST = new Response(400, "请求参数不正确");
    Response UNAUTHORIZED = new Response(401, "账号未登录");
    Response FORBIDDEN = new Response(403, "没有该操作权限");
    Response NOT_FOUND = new Response(404, "请求未找到");
    Response METHOD_NOT_ALLOWED = new Response(405, "请求方法不正确");
    Response LOCKED = new Response(423, "请求失败，请稍后重试"); // 并发请求，不允许
    Response TOO_MANY_REQUESTS = new Response(429, "请求过于频繁，请稍后重试");

    // ========== 服务端错误段 ==========

    Response INTERNAL_SERVER_ERROR = new Response(500, "系统异常");
    Response NOT_IMPLEMENTED = new Response(501, "功能未实现/未开启");

    // ========== 自定义错误段 ==========
    Response REPEATED_REQUESTS = new Response(900, "重复请求，请稍后重试"); // 重复请求
    Response DEMO_DENY = new Response(901, "演示模式，禁止写操作");

    Response UNKNOWN = new Response(999, "未知错误");
    /**
     * 是否为服务端错误，参考 HTTP 5XX 错误码段
     *
     * @param code 错误码
     * @return 是否
     */
    static boolean isServerErrorCode(Integer code) {
        return code != null && code >= INTERNAL_SERVER_ERROR.getCode() && code <= INTERNAL_SERVER_ERROR.getCode() + 99;
    }
}
