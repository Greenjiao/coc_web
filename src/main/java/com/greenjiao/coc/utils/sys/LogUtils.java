package com.greenjiao.coc.utils.sys;

/**
 * @Author Yan
 * @Date 2023/06/05 17:20
 * @Version 1.0
 */
public class LogUtils
{
    public static String getBlock(Object msg)
    {
        if (msg == null)
        {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }
}
