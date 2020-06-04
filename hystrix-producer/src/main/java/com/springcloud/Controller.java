package com.springcloud;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController

public class Controller {
    @Resource
    ProduceService service;

    @GetMapping("/api/v1/demo/get")
    public String produce(){
        return service.produce();
    }
}
