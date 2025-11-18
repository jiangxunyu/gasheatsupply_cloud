package com.chder.gasheatsupply_cloud.config;

import com.chder.gasheatsupply_cloud.dto.UnitBtotResult;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineConfig {
    @Bean
    public Cache<String, UnitBtotResult> calculationCache() {
        return Caffeine.newBuilder()
                // 缓存最多500个计算结果（根据参数组合数量调整）
                .maximumSize(500)
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build();
    }
}
