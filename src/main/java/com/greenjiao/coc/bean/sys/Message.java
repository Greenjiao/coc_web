package com.greenjiao.coc.bean.sys;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author Yan
 * @Date 2023/06/11 7:46
 * @Version 1.0
 */
@Data
public class Message {
    private String from;
    private String to;
    private String msg;
    private LocalDateTime time;
}
