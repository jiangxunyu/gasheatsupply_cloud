package com.chder.gasheatsupply_cloud.feign;

import com.chder.gasheatsupply_cloud.dto.PriceInfoDto;
import com.chder.gasheatsupply_cloud.model.LoadOptimizeForecastParams;
import com.chder.gasheatsupply_cloud.model.PriceInfo;
import com.chder.gasheatsupply_cloud.model.User;
import com.chder.gasheatsupply_cloud.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
@FeignClient(name = "gasheatsupply")
public interface ProviderClient {
    @GetMapping("/hello")  // 目标服务的接口路径
    String sayHello();

    @GetMapping("/hello1")  // 目标服务的接口路径
    String sayHello1();

    @GetMapping("/helloName")  // 目标服务的接口路径
    String helloName(@RequestParam("name") String name);

    @PostMapping("/getUser")
    String getUser(@RequestBody User user);

    /**
     * 供热系统图
     * @return
     */
    @GetMapping("/gasSupply/supplyHeatFigure")
    Result supplyHeatFigure();

    /**
     * 耗能数据
     * @param district
     * @return
     */
    @RequestMapping("/gasSupply/expendEnergyData")
    Result expendEnergyData(@RequestParam(required = false) String district);

    /**
     * 供气成本折线图查询
     * @param district
     * @param date
     * @return
     */
    @RequestMapping("/gasSupply/energyCostData")
    Result energyCostData(@RequestParam(required = false) String district,@RequestParam(required = false) String date);

    /**
     * 供气成本折线图导出
     * @param response
     * @param district
     * @return
     */
    @GetMapping("/gasSupply/exportCostData")
    Result exportCostData(HttpServletResponse response, @RequestParam(required = false) String district,
                          @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate);

    /**
     * 价格配置信息查询
     * @return
     */
    @PostMapping("/gasSupply/queryPriceInfo")
    Result queryPriceInfo(@RequestBody PriceInfoDto priceInfoDto);

    /**
     * 查询最新配置价格信息
     * @return
     */
    @RequestMapping("/gasSupply/getLatestPriceInfo")
    Result getLatestPriceInfo();

    @PostMapping("/gasSupply/addPriceInfo")
    Result addPriceInfo(@RequestBody List<PriceInfo> priceInfos);

    @PostMapping("/gasSupply/deletePriceInfo")
    Result deletePriceInfo(@RequestBody List<String> ids);

    /**
     * 负荷优化分配计算
     * @param forecastParams
     * @return
     */
    @PostMapping("/loadOptimizeAllotCalculate")
    Result loadOptimizeAllotCalculate(@RequestBody LoadOptimizeForecastParams forecastParams);

    /**
     * 获取负荷优化分配计算历史参数
     * @return
     */
    @GetMapping("/getHistoryForecastDataCache")
    Result getHistoryForecastDataCache();

    /**
     * 负荷优化分配方案结果导出
     * @param response
     * @return
     */
    @PostMapping("/exportUnitLoadAllotData")
    Result exportUnitLoadAllotData(HttpServletResponse response);
}
