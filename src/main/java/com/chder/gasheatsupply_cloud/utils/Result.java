package com.chder.gasheatsupply_cloud.utils;

import com.chder.gasheatsupply_cloud.enums.ErrorMsg;
import org.springframework.http.HttpStatus;

/**
 * 接口返回结果
 * @param <T>
 */
public class Result<T> {

    private Integer code;

    private String msg;

    private T data;

    public static Result success() {
        Result result = new Result();
        result.setMsg("操作成功");
        result.setCode(HttpStatus.OK.value());
        return result;
    }

    public static<T> Result success(String msg,T data) {
        Result result = new Result();
        result.setMsg(msg);
        result.setCode(HttpStatus.OK.value());
        result.setData(data);
        return result;
    }

    public static <T> Result success(T data) {
        Result<T> result = new Result<>();
        result.setCode(HttpStatus.OK.value());
        result.setData(data);
        return result;
    }

    public static Result fail(ErrorMsg errorMsg) {
        Result result = new Result();
        result.setMsg("操作失败");
        result.setCode(HttpStatus.BAD_REQUEST.value());
        result.setMsg(errorMsg.getMsg());
        return result;
    }

    public static Result fail(String msg) {
        Result result = new Result();
        result.setMsg("操作失败");
        result.setCode(HttpStatus.BAD_REQUEST.value());
        result.setMsg(msg);
        return result;
    }

    public static <T> Result fail(ErrorMsg errorMsg, T data) {
        Result<T> result = new Result<>();
        result.setMsg("操作失败");
        result.setCode(HttpStatus.BAD_REQUEST.value());
        result.setMsg(errorMsg.getMsg());
        result.setData(data);
        return result;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result() {
    }
}
