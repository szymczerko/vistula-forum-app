package com.vistulaforum.user.model.dto;

import lombok.*;

import java.math.BigInteger;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {

    private BigInteger id;
    private String login;
    private Timestamp registerDate;
    private Timestamp latestLogin;

}
