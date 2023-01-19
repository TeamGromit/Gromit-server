package com.example.gromit.dto.challenge;

import java.time.LocalDateTime;

public interface ChallengeMember {
    String getTitle();
    LocalDateTime getStart_date();
    int getGoal();
    int getRecruits();
    String getCurrents();
}
