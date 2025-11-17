package com.chder.gasheatsupply_cloud.model;

import java.io.Serializable;
import java.util.List;

public class EnergyConsume implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 区域，全厂、机组1、机组2
     */
    private String district;

    /**
     * 标煤煤耗量
     */
    private double coalConsumption;

    /**
     * 平均发电煤效率
     */
    private double averagePowerCoalEfficiency;

    /**
     * 总平均供气成本
     */
    private double totalAverageGasCost;

    /**
     * 总中压供气量
     */
    private double totalMediumPressureGas;

    /**
     * 平均中压供气成本
     */
    private double totalAverageMediumPressureGasCost;

    /**
     * 总低压供气量
     */
    private double totalLowPressureGasCost;

    /**
     * 平均低压供气成本
     */
    private double totalAverageLowPressureGasCost;

    /**
     * 一号机组发电煤耗率
     */
    private double onePowerCoalEfficiency;

    /**
     * 二号机组发电煤耗率
     */
    private double twoPowerCoalEfficiency;

    /**
     * 供气成本折线图
     */
    private List<GasCost> gasCostList;

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public double getCoalConsumption() {
        return coalConsumption;
    }

    public void setCoalConsumption(double coalConsumption) {
        this.coalConsumption = coalConsumption;
    }

    public double getAveragePowerCoalEfficiency() {
        return averagePowerCoalEfficiency;
    }

    public void setAveragePowerCoalEfficiency(double averagePowerCoalEfficiency) {
        this.averagePowerCoalEfficiency = averagePowerCoalEfficiency;
    }

    public double getTotalAverageGasCost() {
        return totalAverageGasCost;
    }

    public void setTotalAverageGasCost(double totalAverageGasCost) {
        this.totalAverageGasCost = totalAverageGasCost;
    }

    public double getTotalMediumPressureGas() {
        return totalMediumPressureGas;
    }

    public void setTotalMediumPressureGas(double totalMediumPressureGas) {
        this.totalMediumPressureGas = totalMediumPressureGas;
    }

    public double getTotalAverageMediumPressureGasCost() {
        return totalAverageMediumPressureGasCost;
    }

    public void setTotalAverageMediumPressureGasCost(double totalAverageMediumPressureGasCost) {
        this.totalAverageMediumPressureGasCost = totalAverageMediumPressureGasCost;
    }

    public double getTotalLowPressureGasCost() {
        return totalLowPressureGasCost;
    }

    public void setTotalLowPressureGasCost(double totalLowPressureGasCost) {
        this.totalLowPressureGasCost = totalLowPressureGasCost;
    }

    public double getTotalAverageLowPressureGasCost() {
        return totalAverageLowPressureGasCost;
    }

    public void setTotalAverageLowPressureGasCost(double totalAverageLowPressureGasCost) {
        this.totalAverageLowPressureGasCost = totalAverageLowPressureGasCost;
    }

    public List<GasCost> getGasCostList() {
        return gasCostList;
    }

    public void setGasCostList(List<GasCost> gasCostList) {
        this.gasCostList = gasCostList;
    }

    public double getOnePowerCoalEfficiency() {
        return onePowerCoalEfficiency;
    }

    public void setOnePowerCoalEfficiency(double onePowerCoalEfficiency) {
        this.onePowerCoalEfficiency = onePowerCoalEfficiency;
    }

    public double getTwoPowerCoalEfficiency() {
        return twoPowerCoalEfficiency;
    }

    public void setTwoPowerCoalEfficiency(double twoPowerCoalEfficiency) {
        this.twoPowerCoalEfficiency = twoPowerCoalEfficiency;
    }
}
