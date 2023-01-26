package com.example.gromit.entity;

import com.example.gromit.base.BaseEntity;
import lombok.*;
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
public class UserCharacter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "characters_id")
    private Characters characters;

    //0: 현재 홈 화면에 배치중인 캐릭터, 1: 진화를 시켰고(획득했고) 현재 홈 화면에는 배치되고 있지 않은 캐릭터
    @Column(nullable = false)
    private int status;

    @Column(nullable = false)
    private boolean isDeleted;

    @Builder
    public UserCharacter(UserAccount userAccount, Characters characters, int status) {
        this.userAccount = userAccount;
        this.characters = characters;
        this.status = status;
    }
}
