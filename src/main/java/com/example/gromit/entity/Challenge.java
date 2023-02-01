package com.example.gromit.entity;

import com.example.gromit.base.BaseEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@ToString
@Entity
public class Challenge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

    @OneToMany(mappedBy = "challenge")
    private List<Member> members = new LinkedList<>();

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private int goal;

    @Column(nullable = false)
    private int recruits;

    @Column(nullable = false)
    private boolean isPassword;

    private String password;

    @Column(nullable = false)
    private boolean isDeleted;

    private Challenge(UserAccount userAccount, String title, LocalDate startDate, LocalDate endDate, int goal, int recruits, boolean isPassword, String password, boolean isDeleted) {
        this.userAccount = userAccount;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.goal = goal;
        this.recruits = recruits;
        this.isPassword = isPassword;
        this.password = password;
        this.isDeleted = isDeleted;
    }

    public static Challenge of(UserAccount userAccount, String title, LocalDate startDate, LocalDate endDate, int goal, int recruits,String password, boolean isPassword, boolean isDeleted){
        return new Challenge(userAccount, title, startDate, endDate, goal, recruits, isPassword,password, isDeleted);
    }


}