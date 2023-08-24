package com.example.gromit.entity;

import com.example.gromit.base.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@ToString
@Entity
@Builder
public class UserAccount extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nickname;


//    @OrderBy("createdAt ASC")
//    @OneToMany
//    @ToString.Exclude
//    private final List<UserCharacter> userCharacters = new LinkedList<>();
//
//    @OneToMany
//    @ToString.Exclude
//    private final List<Member> members = new LinkedList<>();


    @Column(nullable = false, length = 255)
    private String githubName;


    @Column(nullable = false)
    private int commits;
    @Column(nullable = false)
    private int todayCommit;
    @Column(nullable = false)
    private boolean isDeleted;

    @Column(nullable = false)
    private boolean isAlarm;
    private Timestamp alarm;

    private LocalDate commitDate;

    /**
     * 애플 로그인 구현 컬럼들
     */

    @Column(nullable = false)
    private String provider;

    @Email
    @Column(nullable = false, length = 255)
    private String email;

    private String refreshToken;

    private UserAccount(String nickname, String githubName, int commits, int todayCommit, boolean isDeleted, boolean isAlarm, String provider, String email) {
        this.nickname = nickname;
        this.githubName = githubName;
        this.commits = commits;
        this.todayCommit = todayCommit;
        this.isDeleted = isDeleted;
        this.isAlarm = isAlarm;
        this.provider = provider;
        this.email = email;
    }

    public static UserAccount of(String nickname, String githubName, int commits, int todayCommit, String provider, String email, boolean isDeleted, boolean isAlarm) {
        return new UserAccount(nickname, githubName, commits, todayCommit, isDeleted, isAlarm, provider, email);
    }

    public boolean isDeleted() {
        return false;
    }

    public boolean isAlarm() {
        return false;
    }

    //추가
    public void reloadCommits(int todayCommit, int commits) {
        this.todayCommit = todayCommit;
        this.commits = commits;
    }

    public void setCommitDate(LocalDate commitDate) {
        this.commitDate = commitDate;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}