package com.chder.gasheatsupply_cloud.model;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * 机组负荷分配
 */
public class UnitLoadAllot {

    /**
     * 机组名称
     */
    @Excel(name = "机组", orderNum = "0")
    private String unitName;

    /**
     * 中压抽汽量（t/h）
     */
    @Excel(name = "中压抽汽量（t/h）", orderNum = "1")
    private double mediumVoltageGasAmount;

    /**
     * 低压抽汽量（t/h）
     */
    @Excel(name = "低压抽汽量（t/h）", orderNum = "2")
    private double lowerVoltageGasAmount;

    /**
     * 电负荷 就是发电功率
     */
    @Excel(name = "发电功率（MW）", orderNum = "3")
    private double electricLoad;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public double getMediumVoltageGasAmount() {
        return mediumVoltageGasAmount;
    }

    public void setMediumVoltageGasAmount(double mediumVoltageGasAmount) {
        this.mediumVoltageGasAmount = mediumVoltageGasAmount;
    }

    public double getLowerVoltageGasAmount() {
        return lowerVoltageGasAmount;
    }

    public void setLowerVoltageGasAmount(double lowerVoltageGasAmount) {
        this.lowerVoltageGasAmount = lowerVoltageGasAmount;
    }

    public double getElectricLoad() {
        return electricLoad;
    }

    public void setElectricLoad(double electricLoad) {
        this.electricLoad = electricLoad;
    }
}
