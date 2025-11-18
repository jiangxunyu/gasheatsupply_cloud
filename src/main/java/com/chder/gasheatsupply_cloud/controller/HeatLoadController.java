package com.chder.gasheatsupply_cloud.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.chder.gasheatsupply_cloud.enums.ErrorMsg;
import com.chder.gasheatsupply_cloud.model.HeatLoadEnergyConsume;
import com.chder.gasheatsupply_cloud.model.LoadOptimizeForecastParams;
import com.chder.gasheatsupply_cloud.model.UnitLoadAllot;
import com.chder.gasheatsupply_cloud.service.HeatLoadService;
import com.chder.gasheatsupply_cloud.utils.HistoryForecastDataCache;
import com.chder.gasheatsupply_cloud.utils.Result;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/heatLoad")
public class HeatLoadController {

    public static final HistoryForecastDataCache<String, Object> dataCache = HistoryForecastDataCache.getInstance();

    @Autowired
    private HeatLoadService heatLoadService;

    /**
     * 负荷优化分配计算
     * @param forecastParams
     * @return
     */
    @PostMapping("/loadOptimizeAllotCalculate")
    @ResponseBody
    public Result loadOptimizeAllotCalculate(@RequestBody LoadOptimizeForecastParams forecastParams) {
        HeatLoadEnergyConsume heatLoadEnergyConsume;
        try {
            heatLoadEnergyConsume = heatLoadService.loadOptimizeAllotCalculate(forecastParams);
        } catch (Exception e) {
            return Result.fail(ErrorMsg.SYSTEM_ERROR);
        }
        return Result.success(heatLoadEnergyConsume);
    }

    /**
     * 获取负荷优化分配计算历史参数
     * @return
     */
    @GetMapping("/getHistoryForecastDataCache")
    @ResponseBody
    public Result getHistoryForecastDataCache() {
        HashMap<String,Object> resultMap;
        try {
            resultMap = heatLoadService.getHistoryForecastDataCache();
        } catch (Exception e) {
            return Result.fail(ErrorMsg.SYSTEM_ERROR);
        }
        return Result.success(resultMap);
    }

    /**
     * 负荷优化分配方案结果导出
     * @param response
     * @return
     */
    @PostMapping("/exportUnitLoadAllotData")
    @ResponseBody
    public Result exportUnitLoadAllotData(HttpServletResponse response){
        HeatLoadEnergyConsume heatLoadEnergyConsume = (HeatLoadEnergyConsume) dataCache.get("heatLoadEnergyConsume");
        if (null != heatLoadEnergyConsume) {
            List<UnitLoadAllot> unitLoadAllotList = heatLoadEnergyConsume.getUnitLoadAllotList();
            if (!CollectionUtils.isEmpty(unitLoadAllotList)){
                double sumLowerVoltageGasAmount = 0.0;
                double sumMediumVoltageGasAmount = 0.0;
                double sumElectricLoad = 0.0;
                //加上合计数据
                for (UnitLoadAllot unitLoadAllot : unitLoadAllotList) {
                    double lowerVoltageGasAmount = unitLoadAllot.getLowerVoltageGasAmount();
                    double mediumVoltageGasAmount = unitLoadAllot.getMediumVoltageGasAmount();
                    double electricLoad = unitLoadAllot.getElectricLoad();
                    sumLowerVoltageGasAmount = sumLowerVoltageGasAmount+lowerVoltageGasAmount;
                    sumMediumVoltageGasAmount = sumMediumVoltageGasAmount+mediumVoltageGasAmount;
                    sumElectricLoad = sumElectricLoad+electricLoad;
                }
                UnitLoadAllot sumUnitLoadAllot = new UnitLoadAllot();
                sumUnitLoadAllot.setUnitName("合计");
                sumUnitLoadAllot.setLowerVoltageGasAmount(sumLowerVoltageGasAmount);
                sumUnitLoadAllot.setMediumVoltageGasAmount(sumMediumVoltageGasAmount);
                sumUnitLoadAllot.setElectricLoad(sumElectricLoad);
                unitLoadAllotList.add(sumUnitLoadAllot);
                //配置导出参数
                ExportParams exportParams = new ExportParams();
                long time = new Date().getTime();
                String fileName = "各机组负荷分配方案-".concat(String.valueOf(time)).concat(".xlsx");
                try {
                    //生成workbook
                    Workbook workbook = ExcelExportUtil.exportExcel(exportParams,UnitLoadAllot.class,unitLoadAllotList);
                    // 4. 设置响应头
                    String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
                    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                    response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName);
                    response.setCharacterEncoding("UTF-8");
                    // 5. 写入响应流
                    workbook.write(response.getOutputStream());
                    workbook.close();
                } catch (IOException e) {
                    return Result.fail(ErrorMsg.SYSTEM_ERROR);
                }
            }
        }
        return Result.success();
    }
}
