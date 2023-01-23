package com.example.gromit.dto.UserCharacter;

import com.example.gromit.entity.Characters;
import com.example.gromit.entity.UserAccount;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostUserCharacterRes {

    private final long chid;
    private final long userid;
    private final int todayCommit;
    private final String chImg;
    private final int level;
    private final String nickname;
    private final int goal;
    private final int commits;

    public PostUserCharacterRes(UserAccount user, Characters characters) {
        this.userid = user.getId();
        this.chid = characters.getId();
        this.todayCommit = user.getTodayCommit();
        this.chImg = characters.getCharacterImg();
        this.level = characters.getLevel();
        this.nickname = user.getNickname();
        this.goal = characters.getGoal();
        this.commits = user.getCommits();
    }

    @Builder
    public PostUserCharacterRes(long userid, long chid, int todayCommit, String chImg, int level, String nickname, int goal, int commits) {
        this.userid = userid;
        this.chid = chid;
        this.todayCommit = todayCommit;
        this.chImg = chImg;
        this.level = level;
        this.nickname = nickname;
        this.goal = goal;
        this.commits = commits;
    }
}
