package com.springcloud;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@DefaultProperties(defaultFallback = "globalFallBack")
public class ProduceService {

    @Value("${server.port}")
    private String serverPort;

    //=========服务降级=============
    @HystrixCommand(fallbackMethod = "produce_TimeOut",commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value = "2000")
    })
    public String produce(){
        return "product from: "+serverPort;
    }

    public String produce_TimeOut(){
        return "producer-time-out"+serverPort;
    }

    public String globalFallBack(){
        return "consumer-failure";
    }

    //==========服务熔断============
    @HystrixCommand(fallbackMethod = "produceCircuitBreaker_fallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"), //开启熔断
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"), //请求次数阈值
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), //熔断时间
            @HystrixProperty(name = "circuitBreaker.errorThresholdPrecentage", value = "60") //失败率阈值
    })
    public String produceCircuitBreaker(){
        return "product from: "+serverPort;
    }

    public String produceCircuitBreaker_fallback(){
        return "producer-circuit-break, try later";
    }
}
