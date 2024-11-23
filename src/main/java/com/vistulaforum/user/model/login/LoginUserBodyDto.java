package com.vistulaforum.user.model.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginUserBodyDto {

    private String login;
    private String password;

}
