package com.chder.gasheatsupply_cloud.model;

import java.util.List;

/**
 * 热负荷煤耗
 */
public class HeatLoadEnergyConsume {

    /**
     * 总标煤煤耗量（t/h）
     */
    private double totalEnergyConsume;

    /**
     * 总供汽煤耗成本（元/h）
     */
    private double totalGasEnergyCost;

    /**
     * 平均发电煤耗率（g/KWh）
     */
    private double avgPowerEnergyConsumeRate;

    private List<UnitLoadAllot> unitLoadAllotList;

    /**
     * 结果是否正常
     */
    private boolean isNormal;

    /**
     * 提示信息
     */
    private String message;

    public double getTotalEnergyConsume() {
        return totalEnergyConsume;
    }

    public void setTotalEnergyConsume(double totalEnergyConsume) {
        this.totalEnergyConsume = totalEnergyConsume;
    }

    public double getTotalGasEnergyCost() {
        return totalGasEnergyCost;
    }

    public void setTotalGasEnergyCost(double totalGasEnergyCost) {
        this.totalGasEnergyCost = totalGasEnergyCost;
    }

    public double getAvgPowerEnergyConsumeRate() {
        return avgPowerEnergyConsumeRate;
    }

    public void setAvgPowerEnergyConsumeRate(double avgPowerEnergyConsumeRate) {
        this.avgPowerEnergyConsumeRate = avgPowerEnergyConsumeRate;
    }

    public List<UnitLoadAllot> getUnitLoadAllotList() {
        return unitLoadAllotList;
    }

    public void setUnitLoadAllotList(List<UnitLoadAllot> unitLoadAllotList) {
        this.unitLoadAllotList = unitLoadAllotList;
    }

    public boolean getIsNormal() {
        return isNormal;
    }

    public void setIsNormal(boolean normal) {
        isNormal = normal;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
