package com.chder.gasheatsupply_cloud.model;

import java.io.Serializable;

public class PriceInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 实时煤价（元/t）
     */
    private Float coalPrice;

    /**
     * 实时水价（元/t）
     */
    private Float waterPrice;

    /**
     * 启用日期
     */
    private String enableDate;

    /**
     * 操作时间
     */
    private String operateTime;

    /**
     * 状态 0：未启用 1：启用
     */
    private String status;

    /**
     * 是否可删，0：否 1：是
     */
    private String isEnableDelete;

    /**
     * 是否删除 0：否 1：是
     */
    private String isDelete;

    /**
     * 操作人id
     */
    private String operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getCoalPrice() {
        return coalPrice;
    }

    public void setCoalPrice(Float coalPrice) {
        this.coalPrice = coalPrice;
    }

    public Float getWaterPrice() {
        return waterPrice;
    }

    public void setWaterPrice(Float waterPrice) {
        this.waterPrice = waterPrice;
    }

    public String getEnableDate() {
        return enableDate;
    }

    public void setEnableDate(String enableDate) {
        this.enableDate = enableDate;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsEnableDelete() {
        return isEnableDelete;
    }

    public void setIsEnableDelete(String isEnableDelete) {
        this.isEnableDelete = isEnableDelete;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
}
