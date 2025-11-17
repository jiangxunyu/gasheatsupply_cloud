package com.chder.gasheatsupply_cloud.model;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;

public class GasCost implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 时间节点
     */
    @Excel(name = "时间", orderNum = "0")
    private String timeStr;

    /**
     * 平均供气成本
     */
    @Excel(name = "平均供气成本", orderNum = "1")
    private double averageGasCost;

    /**
     * 中压供气成本
     */
    @Excel(name = "中压供气成本", orderNum = "2")
    private double mediumPressureGasCost;

    /**
     * 低压供气成本
     */
    @Excel(name = "低压供气成本", orderNum = "3")
    private double lowPressureGasCost;

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public double getAverageGasCost() {
        return averageGasCost;
    }

    public void setAverageGasCost(double averageGasCost) {
        this.averageGasCost = averageGasCost;
    }

    public double getMediumPressureGasCost() {
        return mediumPressureGasCost;
    }

    public void setMediumPressureGasCost(double mediumPressureGasCost) {
        this.mediumPressureGasCost = mediumPressureGasCost;
    }

    public double getLowPressureGasCost() {
        return lowPressureGasCost;
    }

    public void setLowPressureGasCost(double lowPressureGasCost) {
        this.lowPressureGasCost = lowPressureGasCost;
    }
}
