package com.chder.gasheatsupply_cloud.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.chder.gasheatsupply_cloud.config.GasheatSupplyProperties;
import com.chder.gasheatsupply_cloud.dto.PriceInfoDto;
import com.chder.gasheatsupply_cloud.dto.ZWTimingTestPointDT0;
import com.chder.gasheatsupply_cloud.enums.TestPointEnum;
import com.chder.gasheatsupply_cloud.mapper.PriceInfoMapper;
import com.chder.gasheatsupply_cloud.model.EnergyConsume;
import com.chder.gasheatsupply_cloud.model.GasCost;
import com.chder.gasheatsupply_cloud.model.PriceInfo;
import com.chder.gasheatsupply_cloud.service.GasSupplyService;
import com.chder.gasheatsupply_cloud.utils.HttpClientUtil;
import com.hummeling.if97.IF97;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RefreshScope
public class GasSupplyServiceImpl implements GasSupplyService {

    private static final Logger logger = LoggerFactory.getLogger(GasSupplyService.class);

    //标煤热值
    static final double coal = 29307;
    //锅炉效率
    static final double yb = 0.906;
    //管道效率
    static final double yp = 0.99;

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat targetSdf = new SimpleDateFormat("HH:mm");

    @Autowired
    private PriceInfoMapper priceInfoMapper;
    @Autowired
    private HttpClientUtil httpClientUtil;
    @Autowired
    private GasheatSupplyProperties gasheatSupplyProperties;

    /**
     * 获取能耗供气数据
     *
     * @param district
     * @return
     */
    @Override
    public EnergyConsume expendEnergyData(String district) {
        Map<String, Double> resultMap = calculatorExpendEnergy();
        //全厂平均标煤耗量
        Double btot = resultMap.get("Btot");
        //全厂平均标煤耗量
        Double be = resultMap.get("be");
        //全厂平均标煤耗量
        Double pa = resultMap.get("Pa");
        //中压供气量
        Double qm = resultMap.get("Qm");
        //低压供气量
        Double ql = resultMap.get("Ql");
        //中压供汽成本
        Double pm = resultMap.get("Pm");
        //低压供汽成本
        Double pl = resultMap.get("Pl");
        //一号机组发电煤耗率
        Double be1 = resultMap.get("be1");
        //二号机组发电煤耗率
        Double be2 = resultMap.get("be2");
        EnergyConsume consume = new EnergyConsume();
        consume.setDistrict("全厂");
        consume.setCoalConsumption(btot);
        consume.setAveragePowerCoalEfficiency(be);
        consume.setTotalAverageGasCost(pa);
        consume.setTotalMediumPressureGas(qm);
        consume.setTotalLowPressureGasCost(ql);
        consume.setTotalAverageMediumPressureGasCost(pm);
        consume.setTotalAverageLowPressureGasCost(pl);
        consume.setOnePowerCoalEfficiency(be1);
        consume.setTwoPowerCoalEfficiency(be2);
        return consume;
    }

    /**
     * 计算能耗
     */
    public Map<String, Double> calculatorExpendEnergy() {
        HashMap<String, Double> resultMap = new HashMap<>();
        HashMap<String, Object> params = new HashMap<>();
        String[] tags = new String[]{
                "YHPP:20GR01AA015.OUT",
                "YHPP:20GR01AA051.OUT",
                "YHPP:20GR01AA003.OUT",
                "YHPP:20GR01AA009.OUT",
                "YHPP:20GR01AA021.OUT",
                "YHPP:20GR01AA027.OUT",
                "YHPP:20GR01AA033.OUT",
                "YHPP:20GR01AA039.OUT",
                "YHPP:20GR01AA045.OUT",
                "YHPP:20GR01AA067.OUT",
                "YHPP:20GR01AA078.OUT",
                "YHPP:004WSC_010",
                "YHPP:20GR01AA017.OUT",
                "YHPP:20GR01AA053.OUT",
                "YHPP:20GR01AA005.OUT",
                "YHPP:20GR01AA011.OUT",
                "YHPP:20GR01AA023.OUT",
                "YHPP:20GR01AA029.OUT",
                "YHPP:20GR01AA035.OUT",
                "YHPP:20GR01AA041.OUT",
                "YHPP:20GR01AA047.OUT",
                "YHPP:20GR01AA069.OUT",
                "YHPP:20GR01AA080.OUT",
                "YHPP:004WSC_012",
                "YHPP:10MSP",
                "YHPP:10MST",
                "YHPP:1013SIGSEL_721",
                "YHPP:10LBB10CT601",
                "YHPP:20MSP",
                "YHPP:20MST",
                "YHPP:2013SIGSEL_721",
                "YHPP:20LBB10CT601",
                "YHPP:20LOAD",
                "YHPP:10LOAD",
                "YHPP:1030MATH_603",
                "YHPP:A2NAA30CF101",
                "YHPP:1030MATH_12",
                "YHPP:A2NAA10CF101"
        };
        params.put("tags", tags);
        String apiUrl = gasheatSupplyProperties.getUrl() + "/api/v1/sis_analysis/tags/snapshot";
        String s = httpClientUtil.postJson(apiUrl, params);
        Object data = JSONObject.parse(s).get("data");
        if (null != data) {
            List<ZWTimingTestPointDT0> zwTimingTestPointDT0List = JSONArray.parse(JSONObject.toJSONString(data)).toJavaList(ZWTimingTestPointDT0.class);
            if (CollectionUtil.isNotEmpty(zwTimingTestPointDT0List)) {
                //全厂标煤耗量
                double Btot = 0;
                //全厂发电煤耗率
                double be = 0;
                double be1 = 0;
                double be2 = 0;

                Map<String, Double> pointDataMap = TestPointEnum.getValueMapByPointData(zwTimingTestPointDT0List);
                double C5 = pointDataMap.get("C5");
                double I5 = pointDataMap.get("I5");
                double A5 = pointDataMap.get("A5");
                double B5 = pointDataMap.get("B5");
                double D5 = pointDataMap.get("D5");
                double E5 = pointDataMap.get("E5");
                double F5 = pointDataMap.get("F5");
                double G5 = pointDataMap.get("G5");
                double H5 = pointDataMap.get("H5");
                double J5 = pointDataMap.get("J5");
                double K5 = pointDataMap.get("K5");
                double L5 = pointDataMap.get("L5");
                double C3 = pointDataMap.get("C3");
                double I3 = pointDataMap.get("I3");
                double A3 = pointDataMap.get("A3");
                double B3 = pointDataMap.get("B3");
                double D3 = pointDataMap.get("D3");
                double E3 = pointDataMap.get("E3");
                double F3 = pointDataMap.get("F3");
                double G3 = pointDataMap.get("G3");
                double H3 = pointDataMap.get("H3");
                double J3 = pointDataMap.get("J3");
                double K3 = pointDataMap.get("K3");
                double L3 = pointDataMap.get("L3");
                double Tz1 = pointDataMap.get("Tz1");
                double Pz1 = pointDataMap.get("Pz1");
                double Tzz1 = pointDataMap.get("Tzz1");
                double Pzz1 = pointDataMap.get("Pzz1");
                double P1 = pointDataMap.get("P1");
                double Tz2 = pointDataMap.get("Tz2");
                double Pz2 = pointDataMap.get("Pz2");
                double Tzz2 = pointDataMap.get("Tzz2");
                double Pzz2 = pointDataMap.get("Pzz2");
                double P2 = Optional.ofNullable(pointDataMap.get("P2")).orElse(1.0);
                double mm1 = pointDataMap.get("mm1");
                double ml1 = pointDataMap.get("ml1");
                double mm2 = pointDataMap.get("mm2");
                double ml2 = pointDataMap.get("ml2");
                //中压供汽量
                double Qm = C3 + I3;
                //低压供汽量
                double Ql = A3 + B3 + D3 + E3 + F3 + G3 + H3 + J3 + K3 + L3;
                double Hm = C5 + I5;
                double Hl = A5 + B5 + D5 + E5 + F5 + G5 + H5 + J5 + K5 + L5;
                StringBuilder sb = new StringBuilder();
                sb.append("Map参数详情:\n");
                for (Map.Entry<String, Double> entry : pointDataMap.entrySet()) {
                    sb.append("  - ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }
                logger.info(sb.toString());
                /**
                 * 入炉标煤价C
                 * 水价W
                 */
                PriceInfo priceInfo = getLatestEnablePriceInfo();
                if (null == priceInfo) {
                    throw new RuntimeException("当前入炉标煤价及水价数据为空！");
                }
                double C = priceInfo.getCoalPrice();
                double W = priceInfo.getWaterPrice();
                //中压供汽成本
                double Pm = (Hm * 1000 / coal / yp / yb * C + Qm * W) / Qm;
                //低压供汽成本
                double Pl = (Hl * 1000 / coal / yp / yb * C + Ql * W) / Ql;
                //全厂供汽成本
                double Pa = (Pm * Qm + Pl * Ql) / (Qm + Ql);
                try {
                    //一号机组主蒸汽焓
                    double h01 = calculateEnthalpy(Pz1, Tz1);
                    //一号机组再热蒸汽焓
                    double hzr1 = calculateEnthalpy(Pzz1, Tzz1);
                    //二号机组主蒸汽焓
                    double h02 = calculateEnthalpy(Pz2, Tz2);
                    //二号机组再热蒸汽焓
                    double hzr2 = calculateEnthalpy(Pzz2, Tzz2);
                    double M;
                    boolean flag = true;
                    if (!Double.isNaN(mm1)&&!Double.isNaN(mm2)&&!Double.isNaN(ml1)&&!Double.isNaN(ml2)) {
                        mm1 = mm1 * 1000;
                        mm2 = mm2 * 1000;
                        ml1 = ml1 * 1000;
                        ml2 = ml2 * 1000;
                    }else {
                        //接口获取不到对应的值
                        //一号机组低压抽汽流量
                        ml1 = Hl*1000000/hzr1;
                        //二号机组低压抽汽流量
                        ml2 = 0;
                        //一号机组中压抽汽流量
                        mm1 = Hm * 1000000/2/h01;
                        //二号机组中压抽汽流量
                        mm2 = Hm * 1000000/2/h02;
                        flag = false;
                    }
                    /**
                     * 二号机组发电出力 YHPP:20LOAD  Pe2=P2
                     * 一号机组发电出力 YHPP:10LOAD  Pe1=P1
                     */
                    double Pe1 = P1*1000;
                    double Pe2 = P2*1000;
                    double Btot1 = BtotCalculate(Pe1, mm1, ml1);
                    double Btot2 = BtotCalculate(Pe2, mm2, ml2);
                    Btot = Btot1 + Btot2;
                    if (flag) {
                        double M1 = (mm1*h01+ml1*hzr1)/coal/yb/yp/1000;
                        double M2 = (mm2*h02+ml2*hzr2)/coal/yb/yp/1000;
                        M = M1 + M2;
                        be1 = (Btot1-M1)*1000000/Pe1;
                        be2 = (Btot2-M2)*1000000/Pe2;
                        logger.info("-----M1:{},M2:{}",M1,M2);
                        logger.info("-----be1:{},be2:{}",be1,be2);
                    }else {
                        M = (Hm+Hl)*1000000/coal/yp/yb;
                    }
                    be = (Btot-M)*1000000/(Pe1+Pe2);
                    logger.info("-------Btot:{},Btot1:{},Btot2:{},M:{},P1:{},P2:{}\n" +
                            "mm1:{},ml1:{},mm2:{},ml2:{}\n" +
                            "Qm:{},Ql:{},Hm:{},Hl:{}\n" +
                            "Pm:{},Pl:{},Pa:{}",Btot,Btot1,Btot2,M,P1,P2,mm1,ml1,mm2,ml2,Qm,Ql,Hm,Hl,Pm,Pl,Pa);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
                resultMap.put("Btot", Math.round(Btot*100)/100.0);
                resultMap.put("be", Math.round(be*100)/100.0);
                resultMap.put("Pa", Math.round(Pa*100)/100.0);
                resultMap.put("Qm", Math.round(Qm*100)/100.0);
                resultMap.put("Ql", Math.round(Ql*100)/100.0);
                resultMap.put("Pm", Math.round(Pm*100)/100.0);
                resultMap.put("Pl", Math.round(Pl*100)/100.0);
                resultMap.put("be1", Math.round(be1*100)/100.0);
                resultMap.put("be2", Math.round(be2*100)/100.0);
            }
        }
        return resultMap;
    }

    public double BtotCalculate(double Pe, double mm, double ml) {
        // 一次项
        double term1 = -3.24788702e-01;
        double term2 = 3.70562808e-04 * Pe;
        double term3 = 1.29735823e-04 * mm;
        double term4 = 1.34925898e-04 * ml;

        // 二次项
        double term5 = -1.73070283e-10 * Math.pow(Pe, 2);
        double term6 = -1.70373540e-10 * Pe * mm;
        double term7 = -1.17440652e-10 * Pe * ml;
        double term8 = -1.17342053e-12 * Math.pow(mm, 2);
        double term9 = -3.06410660e-11 * mm * ml;
        double term10 = -3.28522990e-11 * Math.pow(ml, 2);

        // 三次项
        double term11 = 8.58471632e-17 * Math.pow(Pe, 3);
        double term12 = 1.75254964e-16 * Math.pow(Pe, 2) * mm;
        double term13 = 1.57928628e-16 * Math.pow(Pe, 2) * ml;
        double term14 = -6.98131797e-20 * Pe * Math.pow(mm, 2);
        double term15 = 2.41124633e-17 * Pe * mm * ml;
        double term16 = 3.00445549e-17 * Pe * Math.pow(ml, 2);
        double term17 = -5.67772378e-19 * Math.pow(mm, 3);
        double term18 = 4.00166812e-18 * Math.pow(mm, 2) * ml;
        double term19 = 7.71533930e-18 * mm * Math.pow(ml, 2);
        double term20 = 5.72080848e-18 * Math.pow(ml, 3);

        // 总和
        return term1 + term2 + term3 + term4 +
                term5 + term6 + term7 + term8 + term9 + term10 +
                term11 + term12 + term13 + term14 + term15 + term16 +
                term17 + term18 + term19 + term20;
    }

    /**
     * 计算蒸汽焓值（kJ/kg）
     *
     * @param pressure    压力（MPa）
     * @param temperature 温度（°C）
     */
    public static double calculateEnthalpy(double pressure, double temperature) {
        // 创建 IF97 实例
        IF97 if97 = new IF97();
        // 计算并返回焓值
        return if97.specificEnthalpyPT(pressure, temperature + 273.15);
    }

    /**
     * 获取区间供汽成本折线图
     *
     * @param district
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<GasCost> getIntervalGasCost(String district, String startDate, String endDate) {
        if (StringUtils.isBlank(startDate)) {
            DateTime now = DateTime.now();
            startDate = dateFormat1.format(now);
        }
        if (StringUtils.isBlank(endDate)) {
            DateTime now = DateTime.now();
            endDate = dateFormat1.format(now);
        }
        startDate = startDate + " 00:00:00";
        endDate = endDate + " 23:59:59";
        String[] tags = new String[]{
                "YHPP:20GR01AA015.OUT",
                "YHPP:20GR01AA051.OUT",
                "YHPP:20GR01AA003.OUT",
                "YHPP:20GR01AA009.OUT",
                "YHPP:20GR01AA021.OUT",
                "YHPP:20GR01AA027.OUT",
                "YHPP:20GR01AA033.OUT",
                "YHPP:20GR01AA039.OUT",
                "YHPP:20GR01AA045.OUT",
                "YHPP:20GR01AA067.OUT",
                "YHPP:20GR01AA078.OUT",
                "YHPP:004WSC_010",
                "YHPP:20GR01AA017.OUT",
                "YHPP:20GR01AA053.OUT",
                "YHPP:20GR01AA005.OUT",
                "YHPP:20GR01AA011.OUT",
                "YHPP:20GR01AA023.OUT",
                "YHPP:20GR01AA029.OUT",
                "YHPP:20GR01AA035.OUT",
                "YHPP:20GR01AA041.OUT",
                "YHPP:20GR01AA047.OUT",
                "YHPP:20GR01AA069.OUT",
                "YHPP:20GR01AA080.OUT",
                "YHPP:004WSC_012"
        };
        List<GasCost> gasCostList = getTagsIntervalData(tags, startDate, endDate, 1800,false);
        return gasCostList;
    }

    /**
     * 获取多个测点间隔数据
     *
     * @param tags
     * @param startDate
     * @param endDate
     * @param interval
     * @return
     */
    public List<GasCost> getTagsIntervalData(String[] tags, String startDate, String endDate, Integer interval,boolean isFlag) {
        List<GasCost> gasCostList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        List<String> tsList = new ArrayList<>();
        Map<String, List<Map<String, String>>> timeDataMap = new HashMap<>();
        String apiUrl = gasheatSupplyProperties.getUrl() + "/api/v1/sis_analysis/history/interval";
        for (String tag : tags) {
            params.put("tag", tag);
            params.put("startTime", startDate);
            params.put("endTime", endDate);
            params.put("interval", interval);
            String s = httpClientUtil.postJson(apiUrl, params);
            Object data = JSONObject.parse(s).get("data");
            if (null != data) {
                List<ZWTimingTestPointDT0> zwTimingTestPointDT0List = JSONArray.parse(JSONObject.toJSONString(data)).toJavaList(ZWTimingTestPointDT0.class);
                if (CollectionUtil.isNotEmpty(zwTimingTestPointDT0List)) {
                    if (CollectionUtil.isEmpty(tsList)) {
                        //初始保存测点时间
                        for (ZWTimingTestPointDT0 zwTimingTestPointDT0 : zwTimingTestPointDT0List) {
                            String ts = zwTimingTestPointDT0.getTs();
                            tsList.add(ts);
                        }
                    }
                    for (ZWTimingTestPointDT0 zwTimingTestPointDT0 : zwTimingTestPointDT0List) {
                        String ts = zwTimingTestPointDT0.getTs();
                        String value = zwTimingTestPointDT0.getValue();
                        List<Map<String, String>> maps = new ArrayList<>();
                        maps.add(createMap("tag", tag, "value", value));
                        timeDataMap.computeIfAbsent(ts, k -> new ArrayList<>()).addAll(maps);
                    }
                }
            }
        }
        /**
         * 入炉标煤价C
         * 水价W
         */
        PriceInfo priceInfo = getLatestEnablePriceInfo();
        if (null == priceInfo) {
            throw new RuntimeException("当前入炉标煤价及水价数据为空！");
        }
        double C = priceInfo.getCoalPrice();
        double W = priceInfo.getWaterPrice();
        for (String ts : tsList) {
            List<Map<String, String>> mapList = timeDataMap.get(ts);
            if (CollectionUtil.isNotEmpty(mapList)) {
                Map<String, Double> pointDataMap = TestPointEnum.getValueMapByTiming(mapList);
                double C5 = pointDataMap.get("C5");
                double I5 = pointDataMap.get("I5");
                double A5 = pointDataMap.get("A5");
                double B5 = pointDataMap.get("B5");
                double D5 = pointDataMap.get("D5");
                double E5 = pointDataMap.get("E5");
                double F5 = pointDataMap.get("F5");
                double G5 = pointDataMap.get("G5");
                double H5 = pointDataMap.get("H5");
                double J5 = pointDataMap.get("J5");
                double K5 = pointDataMap.get("K5");
                double L5 = pointDataMap.get("L5");
                double C3 = pointDataMap.get("C3");
                double I3 = pointDataMap.get("I3");
                double A3 = pointDataMap.get("A3");
                double B3 = pointDataMap.get("B3");
                double D3 = pointDataMap.get("D3");
                double E3 = pointDataMap.get("E3");
                double F3 = pointDataMap.get("F3");
                double G3 = pointDataMap.get("G3");
                double H3 = pointDataMap.get("H3");
                double J3 = pointDataMap.get("J3");
                double K3 = pointDataMap.get("K3");
                double L3 = pointDataMap.get("L3");
                double Hm = C5 + I5;
                double Hl = A5 + B5 + D5 + E5 + F5 + G5 + H5 + J5 + K5 + L5;
                double Qm = C3 + I3;
                double Ql = A3 + B3 + D3 + E3 + F3 + G3 + H3 + J3 + K3 + L3;
                //中压供汽成本
                double Pm = (Hm*1000/coal/yp/yb*C+Qm*W)/Qm;
                //低压供汽成本
                double Pl = (Hl*1000/coal/yp/yb*C+Ql*W)/Ql;
                //平均供汽成本
                double Pa = (Pm*Qm+Pl*Ql)/(Qm+Ql);

                GasCost gasCost = new GasCost();
                if (isFlag){
                    String tsStr = ts;
                    try {
                        Date date = dateFormat.parse(ts);
                        tsStr = targetSdf.format(date);
                    } catch (ParseException e) {
                        logger.error("----------时间解析异常：{}",ts);
                    }
                    gasCost.setTimeStr(tsStr);
                }else {
                    gasCost.setTimeStr(ts);
                }
                gasCost.setAverageGasCost(Math.round(Pa*100)/100.0);
                gasCost.setMediumPressureGasCost(Math.round(Pm*100)/100.0);
                gasCost.setLowPressureGasCost(Math.round(Pl*100)/100.0);
                gasCostList.add(gasCost);
            }
        }
        return gasCostList;
    }

    private static Map<String, String> createMap(String k1, String v1, String k2, String v2) {
        Map<String, String> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    /**
     * 系统供热图
     *
     * @return
     */
    @Override
    public Map<String, Double> supplyHeatFigure() {
        HashMap<String, Double> resultMap = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        String[] tags = new String[]{
                "YHPP:10DEAVTR",
                "YHPP:10MST",
                "YHPP:10MSP",
                "YHPP:01XN_Drh",
                "YHPP:10LBB10CT601",
                "YHPP:1013SIGSEL_721",
                "YHPP:10LOAD",
                "YHPP:20DEAVTR",
                "YHPP:20MST",
                "YHPP:20MSP",
                "YHPP:02XN_Drh",
                "YHPP:20LBB10CT601",
                "YHPP:2013SIGSEL_721",
                "YHPP:20LOAD",
                "YHPP:20GR01AA015.OUT",
                "YHPP:20GR01AA051.OUT",
                "YHPP:20GR01AA003.OUT",
                "YHPP:20GR01AA009.OUT",
                "YHPP:20GR01AA021.OUT",
                "YHPP:20GR01AA027.OUT",
                "YHPP:20GR01AA033.OUT",
                "YHPP:20GR01AA039.OUT",
                "YHPP:20GR01AA045.OUT",
                "YHPP:20GR01AA067.OUT",
                "YHPP:20GR01AA078.OUT",
                "YHPP:004WSC_010",
                "YHPP:20GR01AA017.OUT",
                "YHPP:20GR01AA053.OUT",
                "YHPP:20GR01AA005.OUT",
                "YHPP:20GR01AA011.OUT",
                "YHPP:20GR01AA023.OUT",
                "YHPP:20GR01AA029.OUT",
                "YHPP:20GR01AA035.OUT",
                "YHPP:20GR01AA041.OUT",
                "YHPP:20GR01AA047.OUT",
                "YHPP:20GR01AA069.OUT",
                "YHPP:20GR01AA080.OUT",
                "YHPP:004WSC_012"
        };
        params.put("tags", tags);
        String apiUrl = gasheatSupplyProperties.getUrl() + "/api/v1/sis_analysis/tags/snapshot";
        logger.info("----url: " + gasheatSupplyProperties.getUrl());
        String s = httpClientUtil.postJson(apiUrl, params);
        Object data = JSONObject.parse(s).get("data");
        if (null != data) {
            List<ZWTimingTestPointDT0> zwTimingTestPointDT0List = JSONArray.parse(JSONObject.toJSONString(data)).toJavaList(ZWTimingTestPointDT0.class);
            if (CollectionUtil.isNotEmpty(zwTimingTestPointDT0List)) {
                Map<String, Double> pointDataMap = TestPointEnum.getValueMapByPointData(zwTimingTestPointDT0List);
                double C5 = pointDataMap.get("C5");
                double I5 = pointDataMap.get("I5");
                double A5 = pointDataMap.get("A5");
                double B5 = pointDataMap.get("B5");
                double D5 = pointDataMap.get("D5");
                double E5 = pointDataMap.get("E5");
                double F5 = pointDataMap.get("F5");
                double G5 = pointDataMap.get("G5");
                double H5 = pointDataMap.get("H5");
                double J5 = pointDataMap.get("J5");
                double K5 = pointDataMap.get("K5");
                double L5 = pointDataMap.get("L5");
                double C3 = pointDataMap.get("C3");
                double I3 = pointDataMap.get("I3");
                double A3 = pointDataMap.get("A3");
                double B3 = pointDataMap.get("B3");
                double D3 = pointDataMap.get("D3");
                double E3 = pointDataMap.get("E3");
                double F3 = pointDataMap.get("F3");
                double G3 = pointDataMap.get("G3");
                double H3 = pointDataMap.get("H3");
                double J3 = pointDataMap.get("J3");
                double K3 = pointDataMap.get("K3");
                double L3 = pointDataMap.get("L3");
                double Hz1 = pointDataMap.get("Hz1");
                double Tz1 = pointDataMap.get("Tz1");
                double Pz1 = pointDataMap.get("Pz1");
                double Hzz1 = pointDataMap.get("Hzz1");
                double Tzz1 = pointDataMap.get("Tzz1");
                double Pzz1 = pointDataMap.get("Pzz1");
                double P1 = pointDataMap.get("P1");
                double Hz2 = pointDataMap.get("Hz2");
                double Tz2 = pointDataMap.get("Tz2");
                double Pz2 = pointDataMap.get("Pz2");
                double Hzz2 = pointDataMap.get("Hzz2");
                double Tzz2 = pointDataMap.get("Tzz2");
                double Pzz2 = pointDataMap.get("Pzz2");
                double P2 = Optional.ofNullable(pointDataMap.get("P2")).orElse(1.0);
                double Hm = C5 + I5;
                double Hl = A5 + B5 + D5 + E5 + F5 + G5 + H5 + J5 + K5 + L5;
                double Qm = C3 + I3;
                double Ql = A3 + B3 + D3 + E3 + F3 + G3 + H3 + J3 + K3 + L3;
                /**
                 * 入炉标煤价C
                 * 水价W
                 */
                PriceInfo priceInfo = getLatestEnablePriceInfo();
                if (null == priceInfo) {
                    throw new RuntimeException("当前入炉标煤价及水价数据为空！");
                }
                double C = priceInfo.getCoalPrice();
                double W = priceInfo.getWaterPrice();
                //中压供汽成本
                double Pm = (Hm * 1000 / coal / yp / yb * C + Qm * W) / Qm;
                //低压供汽成本
                double Pl = (Hl * 1000 / coal / yp / yb * C + Ql * W) / Ql;

                resultMap.put("Hz1", Hz1);
                resultMap.put("Tz1", Tz1);
                resultMap.put("Pz1", Pz1);
                resultMap.put("Hzz1", Hzz1);
                resultMap.put("Tzz1", Tzz1);
                resultMap.put("Pzz1", Pzz1);
                resultMap.put("P1", P1);
                resultMap.put("Pm", Math.round(Pm*100)/100.0);
                resultMap.put("PL", Math.round(Pl*100)/100.0);
                resultMap.put("Hz2", Hz2);
                resultMap.put("Tz2", Tz2);
                resultMap.put("Pz2", Pz2);
                resultMap.put("Hzz2", Hzz2);
                resultMap.put("Tzz2", Tzz2);
                resultMap.put("Pzz2", Pzz2);
                resultMap.put("P2", P2);
            }
        }
        return resultMap;
    }

    /**
     * 获取供汽成本折线图
     *
     * @param district
     * @return
     */
    @Override
    public List<GasCost> getGasCost(String district, String date) {
        if (StringUtils.isBlank(date)) {
            DateTime now = DateTime.now();
            date = dateFormat1.format(now);
        }
        String startDate = date + " 00:00:00";
        String endDate = date + " 23:59:59";
        String[] tags = new String[]{
                "YHPP:20GR01AA015.OUT",
                "YHPP:20GR01AA051.OUT",
                "YHPP:20GR01AA003.OUT",
                "YHPP:20GR01AA009.OUT",
                "YHPP:20GR01AA021.OUT",
                "YHPP:20GR01AA027.OUT",
                "YHPP:20GR01AA033.OUT",
                "YHPP:20GR01AA039.OUT",
                "YHPP:20GR01AA045.OUT",
                "YHPP:20GR01AA067.OUT",
                "YHPP:20GR01AA078.OUT",
                "YHPP:004WSC_010",
                "YHPP:20GR01AA017.OUT",
                "YHPP:20GR01AA053.OUT",
                "YHPP:20GR01AA005.OUT",
                "YHPP:20GR01AA011.OUT",
                "YHPP:20GR01AA023.OUT",
                "YHPP:20GR01AA029.OUT",
                "YHPP:20GR01AA035.OUT",
                "YHPP:20GR01AA041.OUT",
                "YHPP:20GR01AA047.OUT",
                "YHPP:20GR01AA069.OUT",
                "YHPP:20GR01AA080.OUT",
                "YHPP:004WSC_012"
        };
        List<GasCost> gasCostsList = getTagsIntervalData(tags, startDate, endDate, 1800,true);
        return gasCostsList;
    }

    /**
     * 查询价格信息
     *
     * @return
     */
    @Override
    public Map<String, Object> queryPriceInfo(PriceInfoDto priceInfoDto) {
        HashMap<String, Object> resultMap = new HashMap<>();
        priceInfoDto = Optional.ofNullable(priceInfoDto).orElse(new PriceInfoDto());
        //默认查10条
        int startRow = 0;
        int startPage = 0;
        int pageSize = 10;
        if (null != priceInfoDto.getPage()) {
            Integer page = priceInfoDto.getPage();
            if (page > 1) {
                startPage = page - 1;
            }
        }
        if (null != priceInfoDto.getPageSize()) {
            pageSize = priceInfoDto.getPageSize();
        }
        if (startPage > 0) {
            startRow = startPage * pageSize;
        }
        int count = priceInfoMapper.queryCount(priceInfoDto);
        List<PriceInfo> priceInfoList = priceInfoMapper.queryList(priceInfoDto, startRow, pageSize);
        resultMap.put("count", count);
        resultMap.put("data", priceInfoList);
        return resultMap;
    }

    /**
     * 新增价格信息
     *
     * @param priceInfo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPriceInfo(PriceInfo priceInfo) {
        String id = UUID.randomUUID().toString();
        priceInfo.setId(id);
        DateTime now = DateTime.now();
        String format = dateFormat.format(now);
        String format1 = dateFormat1.format(now);
        //先把其他状态变成未启用
        LambdaUpdateWrapper<PriceInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.ge(PriceInfo::getStatus, "1").set(PriceInfo::getStatus, "0");
        priceInfoMapper.update(null, updateWrapper);
        priceInfo.setOperateTime(format);
        priceInfo.setStatus("1");
        priceInfo.setEnableDate(format1);
        priceInfoMapper.addPriceInfo(priceInfo);
    }

    /**
     * 删除价格信息
     *
     * @param ids
     */
    @Override
    public void deletePriceInfo(List<String> ids) {
        int i = priceInfoMapper.deleteBatchIds(ids);
        if (i < 0) {
            throw new RuntimeException("删除失败！");
        }
    }

    /**
     * 批量新增价格配置信息
     *
     * @param priceInfos
     */
    @Override
    public void addBatchPriceInfo(List<PriceInfo> priceInfos) {
        DateTime now = DateTime.now();
        String format = dateFormat.format(now);
        for (PriceInfo priceInfo : priceInfos) {
            String id = UUID.randomUUID().toString();
            priceInfo.setId(id);
            priceInfo.setOperateTime(format);
            priceInfo.setIsEnableDelete("1");
        }
        priceInfoMapper.addBatchPriceInfo(priceInfos);
    }

    /**
     * 查询最新配置价格信息
     *
     * @return
     */
    @Override
    public PriceInfo getLatestPriceInfo() {
        PriceInfo priceInfo = priceInfoMapper.getLatestPriceInfo();
        return priceInfo;
    }

    /**
     * 获取最新启用的价格信息
     *
     * @return
     */
    public PriceInfo getLatestEnablePriceInfo() {
        LambdaQueryWrapper<PriceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(PriceInfo::getStatus, "1").ge(PriceInfo::getIsDelete, "0");
        PriceInfo priceInfo = priceInfoMapper.selectOne(queryWrapper);
        return priceInfo;
    }
}
