package com.chder.gasheatsupply_cloud.controller;

import com.chder.gasheatsupply_cloud.dto.PriceInfoDto;
import com.chder.gasheatsupply_cloud.feign.ProviderClient;
import com.chder.gasheatsupply_cloud.model.PriceInfo;
import com.chder.gasheatsupply_cloud.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "/gasSupplyConsumer")
public class GasSupplyConsumerController {
    private static final Logger log = LoggerFactory.getLogger(GasSupplyConsumerController.class);
    @Autowired
    private ProviderClient providerClient;

    /**
     * 供热系统图
     * @return
     */
    @GetMapping("/supplyHeatFigure")
    @ResponseBody
    public Result supplyHeatFigure() {
        Result result = providerClient.supplyHeatFigure();
        return result;
    }

    /**
     * 耗能数据
     * @param district
     * @return
     */
    @RequestMapping("/expendEnergyData")
    @ResponseBody
    public Result expendEnergyData(@RequestParam(required = false) String district) {
        return providerClient.expendEnergyData(district);
    }

    /**
     * 供气成本折线图查询
     * @param district
     * @param date
     * @return
     */
    @RequestMapping("/energyCostData")
    @ResponseBody
    public Result energyCostData(@RequestParam(required = false) String district,@RequestParam(required = false) String date) {
        return providerClient.energyCostData(district, date);
    }

    /**
     * 供气成本折线图导出
     * @param response
     * @param district
     * @return
     */
    @GetMapping("/exportCostData")
    @ResponseBody
    public Result exportCostData(HttpServletResponse response, @RequestParam(required = false) String district,
                                 @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate) {
        return providerClient.exportCostData(response,district,startDate,endDate);
    }

    /**
     * 价格配置信息查询
     * @return
     */
    @PostMapping("/queryPriceInfo")
    @ResponseBody
    public Result queryPriceInfo(@RequestBody PriceInfoDto priceInfoDto){
        return providerClient.queryPriceInfo(priceInfoDto);
    }

    /**
     * 查询最新配置价格信息
     * @return
     */
    @RequestMapping("/getLatestPriceInfo")
    @ResponseBody
    public Result getLatestPriceInfo(){
        return providerClient.getLatestPriceInfo();
    }

    @PostMapping("addPriceInfo")
    @ResponseBody
    public Result addPriceInfo(@RequestBody List<PriceInfo> priceInfos){
        return providerClient.addPriceInfo(priceInfos);
    }

    @PostMapping("deletePriceInfo")
    @ResponseBody
    public Result deletePriceInfo(@RequestBody List<String> ids){
        return providerClient.deletePriceInfo(ids);
    }
}
