package com.example.gromit.service;

import com.example.gromit.exception.NotFoundException;
import com.example.gromit.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.example.gromit.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;


    @Override
    public UserDetails loadUserByUsername(String userAccountId) throws UsernameNotFoundException{
        System.out.println("userAccountId = " + userAccountId);
        return (UserDetails) userAccountRepository.findByIdAndIsDeleted(Long.parseLong(userAccountId),false)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
    }
}
