package com.chder.gasheatsupply_cloud.controller;

import com.chder.gasheatsupply_cloud.config.UserConfig;
import com.chder.gasheatsupply_cloud.feign.ProviderClient;
import com.chder.gasheatsupply_cloud.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ConsumerController {
    @Autowired
    private ProviderClient providerClient;

    @Autowired
    private UserConfig userConfig;

    @GetMapping("/call")
    public String callProvider() {
        return "Response from provider: " + providerClient.sayHello();
    }

    @GetMapping("/call1")
    public String callProvider1() {
        return "Response from provider: " + providerClient.sayHello1();
    }

    @GetMapping("/call2")
    public String callProvider2(String name) {
        return "Response from provider: " + providerClient.helloName(name);
    }

    @GetMapping("/getUser")
    public String getUser() {
        User user = new User();
        user.setName(userConfig.getName());
        return "Response from provider: " + providerClient.getUser(user);
    }

    @GetMapping("/hello")
    public String sayHello(){
        return "hello world";
    }

    @GetMapping("/hello1")
    public String sayHello1(){
        return userConfig.getName();
    }

    @GetMapping("/helloName")
    public String helloName(@RequestParam("name") String name){
        return name;
    }

    @PostMapping("/getUser")
    String getUser(@RequestBody User user){
        return user.getName();
    }
}

