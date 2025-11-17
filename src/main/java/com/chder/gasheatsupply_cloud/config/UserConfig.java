package com.chder.gasheatsupply_cloud.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "user.one")
@Data
public class UserConfig {
    private String name;
    private int age;
    // getter/setter
}
