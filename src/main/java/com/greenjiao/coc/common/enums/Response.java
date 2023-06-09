package com.greenjiao.coc.common.enums;

import lombok.Data;

/**
 * @Author Yan
 * @Date 2023/06/09 0:00
 * @Version 1.0
 */
@Data
public class Response {

    /**
     * 响应码
     */
    private final Integer code;
    /**
     * 提示
     */
    private final String msg;

    public Response(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }
}
