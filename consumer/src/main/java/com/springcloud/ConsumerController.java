package com.springcloud;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
public class ConsumerController {

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/api/v1/demo/get")
    public String getProduct(){
        return restTemplate.getForObject("http://CLOUD-PRODUCER-SERVICE/get",String.class);
    }
}
