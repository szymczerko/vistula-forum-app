package com.vistulaforum.result;

import com.vistulaforum.post.model.PostCreateState;
import com.vistulaforum.topic.model.create.TopicCreateState;
import com.vistulaforum.user.model.login.LoginUserState;
import com.vistulaforum.user.model.register.RegisterUserState;
import lombok.Getter;

@Getter
public class Result {

    private final int code;
    private final String message;

    public Result(RegisterUserState registerUserState) {
        this.code = registerUserState.getCode();
        this.message = registerUserState.getMessage();
    }

    public Result(LoginUserState loginUserState) {
        this.code = loginUserState.getCode();
        this.message = loginUserState.getMessage();
    }

    public Result(TopicCreateState topicCreateState) {
        this.code = topicCreateState.getCode();
        this.message = topicCreateState.getMessage();
    }

    public Result(PostCreateState postCreateState) {
        this.code = postCreateState.getCode();
        this.message = postCreateState.getMessage();
    }

}
