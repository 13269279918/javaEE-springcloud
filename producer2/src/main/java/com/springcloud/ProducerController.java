package com.springcloud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class ProducerController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping(value = "/get")
    public String getProduct(){
        return "product from "+serverPort;
    }
}
