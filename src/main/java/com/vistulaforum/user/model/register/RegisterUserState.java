package com.vistulaforum.user.model.register;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RegisterUserState {

    OK(200, "OK"),

    LOGIN_TAKEN(409, "Login is already taken");

    private final int code;
    private final String message;

}
