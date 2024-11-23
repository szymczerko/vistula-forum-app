package com.vistulaforum.user.model.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LoginUserState {

    OK(200, "OK"),

    WRONG_LOGIN(401, "User with this login not found"),
    WRONG_PASSWORD(401, "Wrong password");

    private final int code;
    private final String message;

}
