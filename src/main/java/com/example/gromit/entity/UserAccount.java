package com.example.gromit.entity;

import com.example.gromit.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@ToString
@Entity
public class UserAccount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<Challenge> challenges = new LinkedList<>();

    @OrderBy("createdAt ASC")
    @OneToMany
    private List<UserCharacter> userCharacters = new LinkedList<>();

    @OneToMany
    private List<Member> members = new LinkedList<>();

    @Column(nullable = false,length = 255)
    private String email;

    @Column(nullable = false,length = 255)
    private String githubName;

    @Column(nullable = false,length = 50)
    private String nickname;

    @Column(nullable = false)
    private String provider;
    @Column(nullable = false)
    private int commits;
    @Column(nullable = false)
    private int todayCommit;
    @Column(nullable = false)
    private boolean isDeleted;

    @Column(nullable = false)
    private boolean isAlarm;
    private Timestamp alarm;

    private UserAccount(String email, String githubName, String nickname, String provider, int commits, int todayCommit, boolean isDeleted, boolean isAlarm) {
        this.email = email;
        this.githubName = githubName;
        this.nickname = nickname;
        this.provider = provider;
        this.commits = commits;
        this.todayCommit = todayCommit;
        this.isDeleted = isDeleted;
        this.isAlarm = isAlarm;
    }

    public static UserAccount of(String email, String githubName, String nickname, String provider, int commits, int todayCommit, boolean isDeleted, boolean isAlarm) {
        return new UserAccount(email, githubName, nickname, provider, commits, todayCommit, isDeleted, isAlarm);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount)) return false;
        UserAccount that = (UserAccount) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }



}
