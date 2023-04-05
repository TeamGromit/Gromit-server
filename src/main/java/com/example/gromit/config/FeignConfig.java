package com.example.gromit.config;

import com.example.gromit.feign.FeignErrorDecoder;
import com.example.gromit.feign.FeignInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public FeignInterceptor feignInterceptor(){
        return FeignInterceptor.of();
    }

    @Bean
    public FeignErrorDecoder feignErrorDecoder(){
        return FeignErrorDecoder.of();
    }
}
