package com.example.gromit.entity;


import com.example.gromit.base.BaseEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@ToString
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

    @Column(nullable = false)
    private int commits;

    @Column(nullable = false)
    private boolean isDeleted;

    private Member(Challenge challenge, UserAccount userAccount, int commits, boolean isDeleted) {
        this.challenge = challenge;
        this.userAccount = userAccount;
        this.commits = commits;
        this.isDeleted = isDeleted;
    }

    public static Member of(Challenge challenge, UserAccount userAccount, int commits, boolean isDeleted) {
        return new Member(challenge, userAccount, commits, isDeleted);
    }
}