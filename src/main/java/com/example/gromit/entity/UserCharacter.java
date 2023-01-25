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
    @JoinColumn(name="characters_id")
    private Characters characters;

    private String status;

    @Builder
    public UserCharacter(UserAccount userAccount, Characters characters, String status) {
        this.userAccount = userAccount;
        this.characters = characters;
        this.status = status;
    }
}
