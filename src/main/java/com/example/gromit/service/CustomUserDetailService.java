package com.example.gromit.service;

import com.example.gromit.exception.NotFoundException;
import com.example.gromit.exception.UnauthorizedException;
import com.example.gromit.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;


    @Override
    public UserDetails loadUserByUsername(String userAccountId) throws UsernameNotFoundException {
        System.out.println("userAccountId = " + userAccountId);
        return (UserDetails) userAccountRepository.findById(Long.parseLong(userAccountId))
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
    }
}
