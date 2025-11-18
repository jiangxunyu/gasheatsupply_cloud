package com.chder.gasheatsupply_cloud.dto;

/**
 * 主网时序测点数据
 */
public class ZWTimingTestPointDT0 {
    /**
     * 测点时间
     */
    private String ts;
    /**
     * 测点值
     */
    private String value;
    /**
     * 测点位置标签
     */
    private String tag;
    /**
     * 测点位置名称
     */
    private String tagName;

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
