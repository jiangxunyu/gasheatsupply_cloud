package com.chder.gasheatsupply_cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chder.gasheatsupply_cloud.dto.PriceInfoDto;
import com.chder.gasheatsupply_cloud.model.PriceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PriceInfoMapper extends BaseMapper<PriceInfo> {

    List<PriceInfo> queryList(@Param("item") PriceInfoDto priceInfoDto, Integer startRow, Integer pageSize);

    void addPriceInfo(@Param("item") PriceInfo priceInfo);

    int queryCount(@Param("item") PriceInfoDto priceInfoDto);

    void addBatchPriceInfo(@Param("list") List<PriceInfo> priceInfos);

    PriceInfo getLatestPriceInfo();
}
