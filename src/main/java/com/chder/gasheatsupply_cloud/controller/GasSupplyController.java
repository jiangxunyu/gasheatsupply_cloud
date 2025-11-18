package com.chder.gasheatsupply_cloud.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.chder.gasheatsupply_cloud.dto.PriceInfoDto;
import com.chder.gasheatsupply_cloud.enums.ErrorMsg;
import com.chder.gasheatsupply_cloud.model.EnergyConsume;
import com.chder.gasheatsupply_cloud.model.GasCost;
import com.chder.gasheatsupply_cloud.model.PriceInfo;
import com.chder.gasheatsupply_cloud.service.GasSupplyService;
import com.chder.gasheatsupply_cloud.utils.Result;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/gasSupply")
public class GasSupplyController {

    private static final Logger log = LoggerFactory.getLogger(GasSupplyController.class);
    @Autowired
    private GasSupplyService gasSupplyService;

    /**
     * 供热系统图
     * @return
     */
    @GetMapping("/supplyHeatFigure")
    @ResponseBody
    public Result supplyHeatFigure() {
        Map<String,Double> resultMap;
        try {
            resultMap = gasSupplyService.supplyHeatFigure();
        } catch (Exception e) {
            return Result.fail(ErrorMsg.SYSTEM_ERROR);
        }
        return Result.success(resultMap);
    }

    /**
     * 耗能数据
     * @param district
     * @return
     */
    @RequestMapping("/expendEnergyData")
    @ResponseBody
    public Result expendEnergyData(@RequestParam(required = false) String district) {
        EnergyConsume energyConsumeGasCost;
        try {
            energyConsumeGasCost = gasSupplyService.expendEnergyData(district);
        } catch (Exception e) {
            return Result.fail(ErrorMsg.SYSTEM_ERROR);
        }
        return Result.success(energyConsumeGasCost);
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
        List<GasCost> gasCostList;
        try {
            gasCostList = gasSupplyService.getGasCost(district,date);
        } catch (Exception e) {
            return Result.fail(ErrorMsg.SYSTEM_ERROR);
        }
        return Result.success(gasCostList);
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
                                 @RequestParam(required = false) String startDate,@RequestParam(required = false) String endDate) {
        List<GasCost> gasCostList = gasSupplyService.getIntervalGasCost(district,startDate,endDate);
        if (null != gasCostList) {
            if (!CollectionUtils.isEmpty(gasCostList)){
                long time = new Date().getTime();
                //配置导出参数
                ExportParams exportParams = new ExportParams();
                //生成workbook
                Workbook workbook = ExcelExportUtil.exportExcel(exportParams,GasCost.class,gasCostList);
                String fileName = "供汽成本-".concat(String.valueOf(time)).concat(".xlsx");
                // 5. 写入响应流
                try {
                    String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
                    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                    response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName);
                    response.setCharacterEncoding("UTF-8");

                    workbook.write(response.getOutputStream());
                    workbook.close();
                } catch (IOException e) {
                    return Result.fail(ErrorMsg.SYSTEM_ERROR);
                }
            }
        }
        return Result.success();
    }

    /**
     * 价格配置信息查询
     * @return
     */
    @PostMapping("/queryPriceInfo")
    @ResponseBody
    public Result queryPriceInfo(@RequestBody PriceInfoDto priceInfoDto){
        Map<String,Object> resultMap = gasSupplyService.queryPriceInfo(priceInfoDto);
        return Result.success(resultMap);
    }

    /**
     * 查询最新配置价格信息
     * @return
     */
    @RequestMapping("/getLatestPriceInfo")
    @ResponseBody
    public Result getLatestPriceInfo(){
        PriceInfo latestPriceInfo = gasSupplyService.getLatestPriceInfo();
        return Result.success(latestPriceInfo);
    }

    @PostMapping("addPriceInfo")
    @ResponseBody
    public Result addPriceInfo(@RequestBody List<PriceInfo> priceInfos){
        try {
            if (CollectionUtils.isEmpty(priceInfos)){
                return Result.success();
            }
            if (priceInfos.size() > 1){
                gasSupplyService.addBatchPriceInfo(priceInfos);
            }else{
                gasSupplyService.addPriceInfo(priceInfos.get(0));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.fail(ErrorMsg.SYSTEM_ERROR);
        }
        return Result.success();
    }

    @PostMapping("deletePriceInfo")
    @ResponseBody
    public Result deletePriceInfo(@RequestBody List<String> ids){
        try {
            gasSupplyService.deletePriceInfo(ids);
        } catch (Exception e) {
            return Result.fail(ErrorMsg.SYSTEM_ERROR);
        }
        return Result.success();
    }
}
