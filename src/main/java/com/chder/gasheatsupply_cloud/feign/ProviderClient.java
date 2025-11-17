package com.chder.gasheatsupply_cloud.feign;

import com.chder.gasheatsupply_cloud.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "gasheatsupply")
public interface ProviderClient {
    @GetMapping("/hello")  // 目标服务的接口路径
    String sayHello();

    @GetMapping("/hello1")  // 目标服务的接口路径
    String sayHello1();

    @GetMapping("/helloName")  // 目标服务的接口路径
    String helloName(@RequestParam("name") String name);

    @PostMapping("/getUser")
    String getUser(@RequestBody User user);
}
