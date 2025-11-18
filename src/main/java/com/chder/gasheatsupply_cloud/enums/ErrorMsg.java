package com.chder.gasheatsupply_cloud.enums;

public enum ErrorMsg {

    SYSTEM_ERROR("系统错误");

    private String msg;

    ErrorMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
