package com.chder.gasheatsupply_cloud.service.impl;

import com.chder.gasheatsupply_cloud.dto.UnitBtotResult;
import com.chder.gasheatsupply_cloud.model.HeatLoadEnergyConsume;
import com.chder.gasheatsupply_cloud.model.LoadOptimizeForecastParams;
import com.chder.gasheatsupply_cloud.model.UnitLoadAllot;
import com.chder.gasheatsupply_cloud.service.HeatLoadService;
import com.chder.gasheatsupply_cloud.utils.HistoryForecastDataCache;
import com.chder.gasheatsupply_cloud.utils.UnitOptimizeBtot;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class HeatLoadServiceImpl implements HeatLoadService {

    public static final HistoryForecastDataCache<String, Object> dataCache = HistoryForecastDataCache.getInstance();

    static final double MM_MAX = 200;
    static final double ML_MAX = 250;
    static final double PE_MIN = 250.025;
    static final double PE_MAX = 660;

    @Autowired
    private UnitOptimizeBtot unitOptimizeBtot;

    private final Cache<String, UnitBtotResult> calculationCache;

    public HeatLoadServiceImpl(Cache<String, UnitBtotResult> calculationCache) {
        this.calculationCache = calculationCache;
    }

    /**
     * 负荷优化分配计算
     * @param forecastParams
     * @return
     */
    @Override
    public HeatLoadEnergyConsume loadOptimizeAllotCalculate(LoadOptimizeForecastParams forecastParams) {
        HeatLoadEnergyConsume heatLoadEnergyConsume = new HeatLoadEnergyConsume();
        heatLoadEnergyConsume.setIsNormal(true);
        // 1. 生成唯一缓存key（参数组合的唯一标识）
        String cacheKey = generateCacheKey(forecastParams);
        // 2. 从缓存获取结果：若存在则直接返回，不存在则计算并缓存
        UnitBtotResult result = calculationCache.get(cacheKey, key -> complexCalculation(forecastParams));

        double mmOne = Math.round(result.getMm_one()/10) / 100.0;
        double mlOne = Math.round(result.getMl_one()/10) / 100.0;
        double peOne = Math.round(result.getPe_one()/10) / 100.0;
        double mmTwo = Math.round(result.getMm_two()/10) / 100.0;
        double mlTwo = Math.round(result.getMl_two()/10) / 100.0;
        double peTwo = Math.round(result.getPe_two()/10) / 100.0;
        double pr = Math.round(result.getPr()*100) / 100.0;
        double b = Math.round(result.getB()*100) / 100.0;
        double btot = Math.round(result.getBtot()*100) / 100.0;

        int unitOneStatus = forecastParams.getUnitOneStatus();
        int unitTwoStatus = forecastParams.getUnitTwoStatus();

        if (unitOneStatus == 1 && unitTwoStatus == 1){
            if (mmOne > MM_MAX){
                heatLoadEnergyConsume.setIsNormal(false);
                heatLoadEnergyConsume.setMessage("一号机组主蒸汽抽汽量超上限，计算结果不可用！");
            } else if (mlOne > ML_MAX) {
                heatLoadEnergyConsume.setIsNormal(false);
                heatLoadEnergyConsume.setMessage("一号机组再热蒸汽抽汽量超上限，计算结果不可用！");
            }else if (mmTwo > MM_MAX){
                heatLoadEnergyConsume.setIsNormal(false);
                heatLoadEnergyConsume.setMessage("二号机组主蒸汽抽汽量超上限，计算结果不可用！");
            }else if (mlTwo > ML_MAX) {
                heatLoadEnergyConsume.setIsNormal(false);
                heatLoadEnergyConsume.setMessage("二号机组再热蒸汽抽汽量超上限，计算结果不可用！");
            }else if (peOne < PE_MIN){
                heatLoadEnergyConsume.setIsNormal(false);
                heatLoadEnergyConsume.setMessage("一号机组负荷低于最低负荷限值，计算结果不可用！");
            }else if (peOne > PE_MAX){
                heatLoadEnergyConsume.setIsNormal(false);
                heatLoadEnergyConsume.setMessage("一号机组负荷超限值，计算结果不可用！");
            }else if (peTwo < PE_MIN){
                heatLoadEnergyConsume.setIsNormal(false);
                heatLoadEnergyConsume.setMessage("二号机组负荷低于最低负荷限值，计算结果不可用！");
            }else if (peTwo > PE_MAX){
                heatLoadEnergyConsume.setIsNormal(false);
                heatLoadEnergyConsume.setMessage("二号机组负荷超限值，计算结果不可用！");
            }
        }else {
            if (unitOneStatus == 1){
                if (mmOne > MM_MAX){
                    heatLoadEnergyConsume.setIsNormal(false);
                    heatLoadEnergyConsume.setMessage("一号机组主蒸汽抽汽量超上限，计算结果不可用！");
                } else if (mlOne > ML_MAX) {
                    heatLoadEnergyConsume.setIsNormal(false);
                    heatLoadEnergyConsume.setMessage("一号机组再热蒸汽抽汽量超上限，计算结果不可用！");
                }else if (peOne < PE_MIN){
                    heatLoadEnergyConsume.setIsNormal(false);
                    heatLoadEnergyConsume.setMessage("一号机组负荷低于最低负荷限值，计算结果不可用！");
                }else if (peOne > PE_MAX){
                    heatLoadEnergyConsume.setIsNormal(false);
                    heatLoadEnergyConsume.setMessage("一号机组负荷超限值，计算结果不可用！");
                }
            }else if (unitTwoStatus == 1){
                if (mmTwo > MM_MAX){
                    heatLoadEnergyConsume.setIsNormal(false);
                    heatLoadEnergyConsume.setMessage("二号机组主蒸汽抽汽量超上限，计算结果不可用！");
                }else if (mlTwo > ML_MAX) {
                    heatLoadEnergyConsume.setIsNormal(false);
                    heatLoadEnergyConsume.setMessage("二号机组再热蒸汽抽汽量超上限，计算结果不可用！");
                }else if (peTwo < PE_MIN){
                    heatLoadEnergyConsume.setIsNormal(false);
                    heatLoadEnergyConsume.setMessage("二号机组负荷低于最低负荷限值，计算结果不可用！");
                }else if (peTwo > PE_MAX){
                    heatLoadEnergyConsume.setIsNormal(false);
                    heatLoadEnergyConsume.setMessage("二号机组负荷超限值，计算结果不可用！");
                }
            }
        }

        List<UnitLoadAllot> unitLoadAllots = new ArrayList<>();
        UnitLoadAllot unitLoadAllot1 = new UnitLoadAllot();
        UnitLoadAllot unitLoadAllot2 = new UnitLoadAllot();
        unitLoadAllot1.setUnitName("#机组1");
        unitLoadAllot1.setMediumVoltageGasAmount(mmOne);
        unitLoadAllot1.setLowerVoltageGasAmount(mlOne);
        unitLoadAllot1.setElectricLoad(peOne);
        unitLoadAllot2.setUnitName("#机组2");
        unitLoadAllot2.setMediumVoltageGasAmount(mmTwo);
        unitLoadAllot2.setLowerVoltageGasAmount(mlTwo);
        unitLoadAllot2.setElectricLoad(peTwo);
        unitLoadAllots.add(unitLoadAllot1);
        unitLoadAllots.add(unitLoadAllot2);
        heatLoadEnergyConsume.setUnitLoadAllotList(unitLoadAllots);
        heatLoadEnergyConsume.setTotalEnergyConsume(btot);
        heatLoadEnergyConsume.setTotalGasEnergyCost(pr);
        heatLoadEnergyConsume.setAvgPowerEnergyConsumeRate(b);

        //计算结果缓存起来用于导出，因为每次负荷计算结果都不一致
        dataCache.put("heatLoadEnergyConsume",heatLoadEnergyConsume);
        return heatLoadEnergyConsume;
    }

    public UnitBtotResult complexCalculation(LoadOptimizeForecastParams forecastParams) {
        int unitOneStatus = forecastParams.getUnitOneStatus();
        int unitTwoStatus = forecastParams.getUnitTwoStatus();
        double pCoal = Optional.of(forecastParams.getActualCoalPrice()).orElse(1.0);
        double mm = Optional.of(forecastParams.getMidVolSteamDemandAmount()).orElse(1.0);
        double ml = Optional.of(forecastParams.getLowVolSteamDemandAmount()).orElse(1.0);
        int[] unitSources = forecastParams.getUnitSources();
        double pe = Optional.of(forecastParams.getPe()).orElse(1.0);

        //缓存上次分配优化计算参数
        dataCache.put("unitOneStatus",unitOneStatus);
        dataCache.put("unitTwoStatus",unitTwoStatus);
        dataCache.put("actualCoalPrice",pCoal);
        dataCache.put("midVolSteamDemandAmount",mm);
        dataCache.put("lowVolSteamDemandAmount",ml);
        dataCache.put("unitSources",unitSources);
        dataCache.put("pe",pe);
        if (unitSources.length > 1){
            dataCache.put("unitSourceNames",new String[]{"机组1","机组2"});
        }else {
            if (unitSources.length == 0){
                dataCache.put("unitSourceNames",new String[]{});
            } else {
                if (unitSources[0] == 1){
                    dataCache.put("unitSourceNames",new String[]{"机组1"});
                }else {
                    dataCache.put("unitSourceNames",new String[]{"机组2"});
                }
            }
        }

        UnitBtotResult res = null;
        int scenario;
        if (unitOneStatus == 1 && unitTwoStatus == 1){
            //两台机组同时运行
            if (unitSources.length > 1){
                scenario = 3;
            }else {
                scenario = unitSources[0];
            }
            res = unitOptimizeBtot.runPSO(mm*1000, ml*1000, pe*1000,pCoal,scenario);
        }else if (unitOneStatus == 1){
            //机组1运行
            res = unitOptimizeBtot.unitOneBtot(mm*1000, ml*1000, pe*1000,pCoal);
        }else if (unitTwoStatus == 1){
            //机组2运行
            res = unitOptimizeBtot.unitTwoBtot(mm*1000, ml*1000, pe*1000,pCoal);
        }
        return res;
    }

    /**
     * 生成缓存key（关键：确保相同参数生成相同key）
     * @param forecastParams
     * @return
     */
    private String generateCacheKey(LoadOptimizeForecastParams forecastParams) {
        // 复杂场景：JSON序列化（适合对象、列表等复杂参数）
        try {
            return new ObjectMapper().writeValueAsString(forecastParams);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * 获取负荷优化分配计算历史参数
     * @return
     */
    @Override
    public HashMap<String, Object> getHistoryForecastDataCache() {
        if (dataCache.isEmpty()){
            int[] unitSources = new int[]{1,2};
            dataCache.put("unitOneStatus",1);
            dataCache.put("unitTwoStatus",1);
            dataCache.put("actualCoalPrice",1.0);
            dataCache.put("midVolSteamDemandAmount",1.0);
            dataCache.put("lowVolSteamDemandAmount",1.0);
            dataCache.put("unitSources",unitSources);
            dataCache.put("pe",1.0);
            dataCache.put("unitSourceNames",new String[]{"机组1","机组2"});
        }
        return dataCache;
    }
}
