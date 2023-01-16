package com.example.gromit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

    /**
     * createdBy 기본적으로 넣어줌
     */
    @Bean
    public AuditorAware<String> auditorAware(){
        return () -> Optional.of("cp3"); // 임의의 데이터 TODO : 스프링 시큐리티로 인증 기능을 붙이게 될 때, 수정하자
    }
}
