package com.chder.gasheatsupply_cloud.model;

/**
 * 负荷优化分配计算预测参数
 */
public class LoadOptimizeForecastParams {

    /**
     * 机组1运行状态  1：运行
     */
    private int unitOneStatus;

    /**
     * 机组2运行状态  1：运行
     */
    private int unitTwoStatus;

    /**
     * 实时煤价
     */
    private double actualCoalPrice;

    /**
     * 主蒸汽抽气量
     */
    private double midVolSteamDemandAmount;

    /**
     * 再热抽气量
     */
    private double lowVolSteamDemandAmount;

    /**
     * 再热抽气来源
     */
    private int[] unitSources;

    /**
     * 电负荷需求
     */
    private double pe;

    public int getUnitOneStatus() {
        return unitOneStatus;
    }

    public void setUnitOneStatus(int unitOneStatus) {
        this.unitOneStatus = unitOneStatus;
    }

    public int getUnitTwoStatus() {
        return unitTwoStatus;
    }

    public void setUnitTwoStatus(int unitTwoStatus) {
        this.unitTwoStatus = unitTwoStatus;
    }

    public double getActualCoalPrice() {
        return actualCoalPrice;
    }

    public void setActualCoalPrice(double actualCoalPrice) {
        this.actualCoalPrice = actualCoalPrice;
    }

    public double getMidVolSteamDemandAmount() {
        return midVolSteamDemandAmount;
    }

    public void setMidVolSteamDemandAmount(double midVolSteamDemandAmount) {
        this.midVolSteamDemandAmount = midVolSteamDemandAmount;
    }

    public double getLowVolSteamDemandAmount() {
        return lowVolSteamDemandAmount;
    }

    public void setLowVolSteamDemandAmount(double lowVolSteamDemandAmount) {
        this.lowVolSteamDemandAmount = lowVolSteamDemandAmount;
    }

    public int[] getUnitSources() {
        return unitSources;
    }

    public void setUnitSources(int[] unitSources) {
        this.unitSources = unitSources;
    }

    public double getPe() {
        return pe;
    }

    public void setPe(double pe) {
        this.pe = pe;
    }
}
