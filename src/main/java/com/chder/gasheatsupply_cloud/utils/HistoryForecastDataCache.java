package com.chder.gasheatsupply_cloud.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class HistoryForecastDataCache<K, V> extends LinkedHashMap<K, V> {

    // 1. 缓存容量（根据需求调整）
    private static final int CACHE_SIZE = 9;
    // 2. 单例实例（饿汉式，线程安全）
    private static final HistoryForecastDataCache<?, ?> INSTANCE = new HistoryForecastDataCache<>();

    // 3. 私有构造器，指定LinkedHashMap参数
    private HistoryForecastDataCache() {
        /*
         * initialCapacity：初始容量
         * loadFactor：负载因子（0.75f为默认，平衡时间/空间效率）
         * accessOrder：true=按访问顺序排序（LRU策略），false=按插入顺序排序
         */
        super(CACHE_SIZE, 0.75f, true); // 此处选择LRU策略（最近最少访问的元素优先淘汰）
    }

    // 4. 获取全局单例实例
    @SuppressWarnings("unchecked")
    public static <K, V> HistoryForecastDataCache<K, V> getInstance() {
        return (HistoryForecastDataCache<K, V>) INSTANCE;
    }

    // 5. 重写移除策略：当缓存size超过容量时，自动删除最旧元素
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        // 当缓存大小 > 设定容量时，返回true触发移除最旧元素
        return size() > CACHE_SIZE;
    }

    // 6. 可选：添加线程安全控制（若多线程操作缓存，需加锁）
    @Override
    public synchronized V put(K key, V value) {
        return super.put(key, value);
    }

    @Override
    public synchronized V get(Object key) {
        return super.get(key);
    }
}
