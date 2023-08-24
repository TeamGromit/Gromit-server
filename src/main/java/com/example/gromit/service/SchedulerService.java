package com.example.gromit.service;


import com.example.gromit.entity.UserAccount;
import com.example.gromit.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final UserAccountRepository userAccountRepository;

    private final UserAccountService userAccountService;


    @Scheduled(cron = "0 0 0 1/1 * ? *")
    public void renewAllUserCommit(){
        List<UserAccount> userAccounts = userAccountRepository.findByIsDeleted(false);

        userAccounts.stream().forEach(userAccount -> {
            userAccountService.reloadCommits(userAccount);
            userAccountService.resetTodayCommits(userAccount);

        });
    }
}
