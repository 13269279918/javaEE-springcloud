package com.springcloud;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
@DefaultProperties(defaultFallback = "globalFallBack")
public class ConsumeService {

    @Resource
    private RestTemplate restTemplate;

    //==========服务降级============
    @HystrixCommand(fallbackMethod = "getProduct_TimeOut",commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMillisecond", value = "1500")
    })
    public String getProduct(){
        return restTemplate.getForObject("http://CLOUD-HYSTRIX-PRODUCER-SERVICE/get",String.class);
    }

    public String getProduct_TimeOut(){
        return "consumer-time-out";
    }

    public String globalFallBack(){
        return "consumer-failure";
    }

    //==========服务熔断============
    @HystrixCommand(fallbackMethod = "consumeCircuitBreaker_fallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"), //开启熔断
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"), //请求次数阈值
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), //熔断时间
            @HystrixProperty(name = "circuitBreaker.errorThresholdPrecentage", value = "60") //失败率阈值
    })
    public String consumeCircuitBreaker(){
        return restTemplate.getForObject("http://CLOUD-HYSTRIX-PRODUCER-SERVICE/get",String.class);
    }

    public String consumeCircuitBreaker_fallback(){
        return "consumer-circuit-break, try later";
    }
}
