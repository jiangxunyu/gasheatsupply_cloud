package com.chder.gasheatsupply_cloud.service;

import com.chder.gasheatsupply_cloud.model.HeatLoadEnergyConsume;
import com.chder.gasheatsupply_cloud.model.LoadOptimizeForecastParams;

import java.util.HashMap;

public interface HeatLoadService {
    HeatLoadEnergyConsume loadOptimizeAllotCalculate(LoadOptimizeForecastParams forecastParams);

    HashMap<String, Object> getHistoryForecastDataCache();

}
