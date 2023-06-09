package com.greenjiao.coc.common.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenjiao.coc.common.enums.Response;
import com.greenjiao.coc.common.enums.ResponseEnum;
import com.greenjiao.coc.common.exception.ServerException;
import com.greenjiao.coc.common.exception.ServiceException;
import lombok.Data;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author Yan
 * @Date 2023/06/09 0:13
 * @Version 1.0
 */
@Data
public class CommonResult<T> implements Serializable {

    /**
     * 错误码
     */
    private Integer code;
    /**
     * 返回数据
     */
    private T data;
    /**
     * 错误提示，用户可阅读
     */
    private String msg;

    /**
     * 将传入的 result 对象，转换成另外一个泛型结果的对象
     * <p>
     * 因为 A 方法返回的 CommonResult 对象，不满足调用其的 B 方法的返回，所以需要进行转换。
     *
     * @param result 传入的 result 对象
     * @param <T>    返回的泛型
     * @return 新的 CommonResult 对象
     */
    public static <T> CommonResult<T> error(CommonResult<?> result) {
        return error(result.getCode(), result.getMsg());
    }

    public static <T> CommonResult<T> error(Integer code, String message) {
        Assert.isTrue(!ResponseEnum.SUCCESS.getCode().equals(code), "code 必须是错误的！");
        CommonResult<T> result = new CommonResult<>();
        result.code = code;
        result.msg = message;
        return result;
    }

    public static <T> CommonResult<T> error(Response errorCode) {
        return error(errorCode.getCode(), errorCode.getMsg());
    }

    public static <T> CommonResult<T> error(ServiceException serviceException) {
        return error(serviceException.getCode(), serviceException.getMessage());
    }

    public static <T> CommonResult<T> success(T data) {
        CommonResult<T> result = new CommonResult<>();
        result.code = ResponseEnum.SUCCESS.getCode();
        result.data = data;
        result.msg = "";
        return result;
    }

    public static <T> CommonResult<T> success(T data, String msg) {
        CommonResult<T> result = new CommonResult<>();
        result.code = ResponseEnum.SUCCESS.getCode();
        result.data = data;
        result.msg = msg;
        return result;
    }

    public static boolean isSuccess(Integer code) {
        return Objects.equals(code, ResponseEnum.SUCCESS.getCode());
    }

    @JsonIgnore // 避免 jackson 序列化
    public boolean isSuccess() {
        return isSuccess(code);
    }

    @JsonIgnore // 避免 jackson 序列化
    public boolean isError() {
        return !isSuccess();
    }

    // ========= 和 Exception 异常体系集成 =========

    /**
     * 判断是否有异常。如果有，则抛出 {@link ServiceException} 异常
     */
    public void checkError() {
        if (isSuccess()) {
            return;
        }
        // 服务端异常
        if (ResponseEnum.isServerErrorCode(code)) {
            throw new ServerException(code, msg);
        }
        // 业务异常
        throw new ServiceException(code, msg);
    }

    /**
     * 判断是否有异常。如果有，则抛出 {@link ServiceException} 异常
     * 如果没有，则返回 {@link #data} 数据
     */
    @JsonIgnore // 避免 jackson 序列化
    public T getCheckedData() {
        checkError();
        return data;
    }

}
