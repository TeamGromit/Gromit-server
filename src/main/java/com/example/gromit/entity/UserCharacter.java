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

    // 0 진행중 , 1 완료
    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private boolean isDeleted;

    @Builder
    public UserCharacter(UserAccount userAccount, Characters characters, String status) {
        this.userAccount = userAccount;
        this.characters = characters;
        this.status = status;
    }
}
