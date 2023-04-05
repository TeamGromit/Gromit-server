package com.example.gromit.feign;


import com.example.gromit.config.FeignConfig;
import com.example.gromit.dto.user.response.FeignResponseInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name="githubClient",
        url= "https://api.github.com",
        configuration= FeignConfig.class
)
public interface GithubClient {
    @GetMapping("/users/{githubNickname}")
    FeignResponseInfo getGithubUser(@PathVariable("githubNickname") String githubNickname);

}
