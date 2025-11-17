package com.chder.gasheatsupply_cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ConfigController {

    @Value("${user.one.name:default}")  // 冒号后为默认值
    private String userName;

    @GetMapping("/config")
    public String getConfig() {
        return "User Name from Nacos: " + userName;
    }
}
