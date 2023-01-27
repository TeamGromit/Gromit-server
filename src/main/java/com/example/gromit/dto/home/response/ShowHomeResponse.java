package com.example.gromit.dto.home.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class ShowHomeResponse {

    private final int commits;
    private final int todayCommit;

    private final int level;

    private final String name;

    private final String img;

    private final int goal;

    private ShowHomeResponse(int commits, int todayCommit, int level, String name, String img, int goal) {
        this.commits = commits;
        this.todayCommit = todayCommit;
        this.level = level;
        this.name = name;
        this.img = img;
        this.goal = goal;
    }

    public static ShowHomeResponse of(int commits, int todayCommit, int level, String name, String img, int goal) {
        return new ShowHomeResponse(commits, todayCommit, level, name, img, goal);
    }
}
