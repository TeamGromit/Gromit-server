package com.example.gromit.dto.challenge;

import com.example.gromit.entity.Challenge;
import com.example.gromit.entity.Member;
import com.example.gromit.entity.UserAccount;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Builder
@Setter
@Data
public class PostMemberReq {

    private int commits;

    private boolean isDeleted;

    private Long userAccountId;

    private Long challengeId;


}
