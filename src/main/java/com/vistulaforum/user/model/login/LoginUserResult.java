package com.vistulaforum.user.model.login;

import com.vistulaforum.result.Result;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class LoginUserResult {

    private Result result;
    private UserJwtTokenDetails jwt;

    public LoginUserResult(Result result, String token, Timestamp expiresAt) {
        this.result = result;
        if (token != null) {
            this.jwt = new UserJwtTokenDetails(token, expiresAt);
        }
    }

    @Getter
    private class UserJwtTokenDetails {
        private final String type = "Beaer ";
        private final String token;
        private final Timestamp expiresAt;

        public UserJwtTokenDetails(String token, Timestamp expiresAt) {
            this.token = token;
            this.expiresAt = expiresAt;
        }
    }
}
