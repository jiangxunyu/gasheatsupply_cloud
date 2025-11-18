package com.chder.gasheatsupply_cloud.dto;


import com.chder.gasheatsupply_cloud.model.PriceInfo;

public class PriceInfoDto extends PriceInfo {
    /**
     * 第几页
     */
    private Integer page;

    /**
     * 每页条数
     */
    private Integer pageSize;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
