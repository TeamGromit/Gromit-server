package com.example.gromit.feign;

import com.example.gromit.config.FeignConfig;
import com.example.gromit.dto.user.response.AppleFeignResponseInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name="appleClient",
        url= "https://appleid.apple.com/auth/keys",
        configuration= FeignConfig.class
)
public interface AppleClient {

    @GetMapping
    AppleFeignResponseInfo getAppleKeys();
}
