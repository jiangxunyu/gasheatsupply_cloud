package com.chder.gasheatsupply_cloud.enums;

import com.chder.gasheatsupply_cloud.dto.ZWTimingTestPointDT0;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum TestPointEnum {

    YHPP_20GR01AA015_OUT("YHPP:20GR01AA015.OUT", "C3", "兰石化中压瞬时流量"),
    YHPP_20GR01AA051_OUT("YHPP:20GR01AA051.OUT", "I3", "艾克莱特中压瞬时流量"),
    YHPP_20GR01AA003_OUT("YHPP:20GR01AA003.OUT", "A3", "洪宇瞬时流量"),
    YHPP_20GR01AA009_OUT("YHPP:20GR01AA009.OUT", "B3", "华秦低压瞬时流量"),
    YHPP_20GR01AA021_OUT("YHPP:20GR01AA021.OUT", "D3", "兰石化低压瞬时流量"),
    YHPP_20GR01AA027_OUT("YHPP:20GR01AA027.OUT", "E3", "厂内低压母管瞬时流量"),
    YHPP_20GR01AA033_OUT("YHPP:20GR01AA033.OUT", "F3", "永博瞬时流量"),
    YHPP_20GR01AA039_OUT("YHPP:20GR01AA039.OUT", "G3", "玉珩达瞬时流量"),
    YHPP_20GR01AA045_OUT("YHPP:20GR01AA045.OUT", "H3", "艾克莱特瞬时流量"),
    YHPP_20GR01AA067_OUT("YHPP:20GR01AA067.OUT", "J3", "源鑫环保低压瞬时流量"),
    YHPP_20GR01AA078_OUT("YHPP:20GR01AA078.OUT", "K3", "探微低压瞬时流量"),
    YHPP_004WSC_010("YHPP:004WSC_010", "L3", "污水处理厂瞬时流量"),
    YHPP_20GR01AA017_OUT("YHPP:20GR01AA017.OUT", "C5", "兰石化中压瞬时热量"),
    YHPP_20GR01AA053_OUT("YHPP:20GR01AA053.OUT", "I5", "艾克莱特中压瞬时热量"),
    YHPP_20GR01AA005_OUT("YHPP:20GR01AA005.OUT", "A5", "洪宇瞬时热量"),
    YHPP_20GR01AA011_OUT("YHPP:20GR01AA011.OUT", "B5", "华秦低压瞬时热量"),
    YHPP_20GR01AA023_OUT("YHPP:20GR01AA023.OUT", "D5", "兰石化低压瞬时热量"),
    YHPP_20GR01AA029_OUT("YHPP:20GR01AA029.OUT", "E5", "厂内低压母管瞬时热量"),
    YHPP_20GR01AA035_OUT("YHPP:20GR01AA035.OUT", "F5", "永博瞬时热量"),
    YHPP_20GR01AA041_OUT("YHPP:20GR01AA041.OUT", "G5", "玉珩达瞬时热量"),
    YHPP_20GR01AA047_OUT("YHPP:20GR01AA047.OUT", "H5", "艾克莱特瞬时热量"),
    YHPP_20GR01AA069_OUT("YHPP:20GR01AA069.OUT", "J5", "源鑫环保低压瞬时热量"),
    YHPP_20GR01AA080_OUT("YHPP:20GR01AA080.OUT", "K5", "探微低压瞬时热量"),
    YHPP_004WSC_012("YHPP:004WSC_012", "L5", "污水处理厂瞬时热量"),
    YHPP_10DEAVTR("YHPP:10DEAVTR", "Hz1", "一号机组主蒸汽流量"),
    YHPP_10MST("YHPP:10MST", "Tz1", "一号机组主蒸汽温度"),
    YHPP_10MSP("YHPP:10MSP", "Pz1", "一号机组主蒸汽压力"),
    YHPP_01XN_Drh("YHPP:01XN_Drh", "Hzz1", "一号机组再热蒸汽流量"),
    YHPP_10LBB10CT601("YHPP:10LBB10CT601", "Tzz1", "一号机组再热蒸汽温度"),
    YHPP_1013SIGSEL_721("YHPP:1013SIGSEL_721", "Pzz1", "一号机组再热蒸汽压力"),
    YHPP_10LOAD("YHPP:10LOAD", "P1", "一号机组发电功率"),
    YHPP_20DEAVTR("YHPP:20DEAVTR", "Hz2", "二号机组主蒸汽流量"),
    YHPP_20MST("YHPP:20MST", "Tz2", "二号机组主蒸汽温度"),
    YHPP_20MSP("YHPP:20MSP", "Pz2", "二号机组主蒸汽压力"),
    YHPP_02XN_Drh("YHPP:02XN_Drh", "Hzz2", "二号机组再热蒸汽流量"),
    YHPP_20LBB10CT601("YHPP:20LBB10CT601", "Tzz2", "二号机组再热蒸汽温度"),
    YHPP_2013SIGSEL_721("YHPP:2013SIGSEL_721", "Pzz2", "二号机组再热蒸汽压力"),
    YHPP_20LOAD("YHPP:20LOAD", "P2", "二号机组发电功率"),
    YHPP_1030MATH_12("YHPP:1030MATH_12","mm1","一号机组中压抽汽流量"),
    YHPP_1030MATH_603("YHPP:1030MATH_603","ml1","一号机组低压抽汽流量"),
    YHPP_A2NAA10CF101("YHPP:A2NAA10CF101","mm2","二号机组中压抽汽流量"),
    YHPP_A2NAA30CF101("YHPP:A2NAA30CF101","ml2","YHPP:A2NAA30CF101"),
    ;

    private String code;

    @Getter
    private String key;

    private String value;

    private static final Logger logger = LoggerFactory.getLogger(TestPointEnum.class);

    TestPointEnum(String code, String key, String value) {
        this.code = code;
        this.key = key;
        this.value = value;
    }

    public static TestPointEnum fromCode(String code) {
        for (TestPointEnum pointEnum : TestPointEnum.values()) {
            if (pointEnum.code.equals(code)) {
                return pointEnum;
            }
        }
        return null;
    }

    /**
     * 获取多个时序测点值
     * @param pointDT0List
     * @return
     */
    public static Map<String, Double> getValueMapByPointData(List<ZWTimingTestPointDT0> pointDT0List) {
        HashMap<String, Double> resultMap = new HashMap<>();
        /**
         * 命名有点出路
         * Pe1=P1，Pe2=P2  返回的是P1、P2
         */
        for (ZWTimingTestPointDT0 zwTimingTestPointDT0 : pointDT0List) {
            String tag = zwTimingTestPointDT0.getTag();
            String value = zwTimingTestPointDT0.getValue();
            TestPointEnum pointEnum = TestPointEnum.fromCode(tag);
            if (null != pointEnum) {
                setValueMap(resultMap,value,pointEnum);
            }
        }
        return resultMap;
    }

    /**
     * 获取时序区间测点值
     * @param mapList
     * @return
     */
    public static Map<String,Double> getValueMapByTiming(List<Map<String, String>> mapList){
        HashMap<String, Double> resultMap = new HashMap<>();
        /**
         * 命名有点出路
         * Pe1=P1，Pe2=P2  返回的是P1、P2
         */
        for (Map<String, String> tagValueMap : mapList) {
            String tag = tagValueMap.get("tag");
            String value = tagValueMap.get("value");
            TestPointEnum pointEnum = TestPointEnum.fromCode(tag);
            if (null != pointEnum) {
                setValueMap(resultMap,value,pointEnum);
            }
        }
        return resultMap;
    }

    public static void setValueMap(Map<String, Double> resultMap,String value,TestPointEnum pointEnum){
        try {
            double valueOf = Math.round(Double.parseDouble(value)*100)/100.0;
            switch (pointEnum) {
                case YHPP_20GR01AA015_OUT:
                    double C3 = valueOf;
                    resultMap.put("C3",C3);
                    break;
                case YHPP_20GR01AA051_OUT:
                    double I3 = valueOf;
                    resultMap.put("I3",I3);
                    break;
                case YHPP_20GR01AA003_OUT:
                    double A3 = valueOf;
                    resultMap.put("A3",A3);
                    break;
                case YHPP_20GR01AA009_OUT:
                    double B3 = valueOf;
                    resultMap.put("B3",B3);
                    break;
                case YHPP_20GR01AA021_OUT:
                    double D3 = valueOf;
                    resultMap.put("D3",D3);
                    break;
                case YHPP_20GR01AA027_OUT:
                    double E3 = valueOf;
                    resultMap.put("E3",E3);
                    break;
                case YHPP_20GR01AA033_OUT:
                    double F3 = valueOf;
                    resultMap.put("F3",F3);
                    break;
                case YHPP_20GR01AA039_OUT:
                    double G3 = valueOf;
                    resultMap.put("G3",G3);
                    break;
                case YHPP_20GR01AA045_OUT:
                    double H3 = valueOf;
                    resultMap.put("H3",H3);
                    break;
                case YHPP_20GR01AA067_OUT:
                    double J3 = valueOf;
                    resultMap.put("J3",J3);
                    break;
                case YHPP_20GR01AA078_OUT:
                    double K3 = valueOf;
                    resultMap.put("K3",K3);
                    break;
                case YHPP_004WSC_010:
                    double L3 = valueOf;
                    resultMap.put("L3",L3);
                    break;
                case YHPP_20GR01AA017_OUT:
                    double C5 = valueOf;
                    resultMap.put("C5",C5);
                    break;
                case YHPP_20GR01AA053_OUT:
                    double I5 = valueOf;
                    resultMap.put("I5",I5);
                    break;
                case YHPP_20GR01AA005_OUT:
                    double A5 = valueOf;
                    resultMap.put("A5",A5);
                    break;
                case YHPP_20GR01AA011_OUT:
                    double B5 = valueOf;
                    resultMap.put("B5",B5);
                    break;
                case YHPP_20GR01AA023_OUT:
                    double D5 = valueOf;
                    resultMap.put("D5",D5);
                    break;
                case YHPP_20GR01AA029_OUT:
                    double E5 = valueOf;
                    resultMap.put("E5",E5);
                    break;
                case YHPP_20GR01AA035_OUT:
                    double F5 = valueOf;
                    resultMap.put("F5",F5);
                    break;
                case YHPP_20GR01AA041_OUT:
                    double G5 = valueOf;
                    resultMap.put("G5",G5);
                    break;
                case YHPP_20GR01AA047_OUT:
                    double H5 = valueOf;
                    resultMap.put("H5",H5);
                    break;
                case YHPP_20GR01AA069_OUT:
                    double J5 = valueOf;
                    resultMap.put("J5",J5);
                    break;
                case YHPP_20GR01AA080_OUT:
                    double K5 = valueOf;
                    resultMap.put("K5",K5);
                    break;
                case YHPP_004WSC_012:
                    double L5 = valueOf;
                    resultMap.put("L5",L5);
                    break;
                case YHPP_10DEAVTR:
                    double Hz1 = valueOf;
                    resultMap.put("Hz1",Hz1);
                    break;
                case YHPP_10MST:
                    double Tz1 = valueOf;
                    resultMap.put("Tz1",Tz1);
                    break;
                case YHPP_10MSP:
                    double Pz1 = valueOf;
                    resultMap.put("Pz1",Pz1);
                    break;
                case YHPP_01XN_Drh:
                    double Hzz1 = valueOf;
                    resultMap.put("Hzz1",Hzz1);
                    break;
                case YHPP_10LBB10CT601:
                    double Tzz1 = valueOf;
                    resultMap.put("Tzz1",Tzz1);
                    break;
                case YHPP_1013SIGSEL_721:
                    double Pzz1 = valueOf;
                    resultMap.put("Pzz1",Pzz1);
                    break;
                case YHPP_10LOAD:
                    double P1 = valueOf;
                    resultMap.put("P1",P1);
                    break;
                case YHPP_20DEAVTR:
                    double Hz2 = valueOf;
                    resultMap.put("Hz2",Hz2);
                    break;
                case YHPP_20MST:
                    double Tz2 = valueOf;
                    resultMap.put("Tz2",Tz2);
                    break;
                case YHPP_20MSP:
                    double Pz2 = valueOf;
                    resultMap.put("Pz2",Pz2);
                    break;
                case YHPP_02XN_Drh:
                    double Hzz2 = valueOf;
                    resultMap.put("Hzz2",Hzz2);
                    break;
                case YHPP_20LBB10CT601:
                    double Tzz2 = valueOf;
                    resultMap.put("Tzz2",Tzz2);
                    break;
                case YHPP_2013SIGSEL_721:
                    double Pzz2 = valueOf;
                    resultMap.put("Pzz2",Pzz2);
                    break;
                case YHPP_20LOAD:
                    double P2 = valueOf;
                    resultMap.put("P2",P2);
                    break;
                case YHPP_1030MATH_12:
                    double mm1 = valueOf;
                    resultMap.put("mm1",mm1);
                    break;
                case YHPP_1030MATH_603:
                    double ml1 = valueOf;
                    resultMap.put("ml1",ml1);
                    break;
                case YHPP_A2NAA10CF101:
                    double mm2 = valueOf;
                    resultMap.put("mm2",mm2);
                    break;
                case YHPP_A2NAA30CF101:
                    double ml2 = valueOf;
                    resultMap.put("ml2",ml2);
                    break;
            }
        } catch (NumberFormatException e) {
            logger.error("-----------------返回值解析异常，解析值：{}", value);
        }
    }
}
