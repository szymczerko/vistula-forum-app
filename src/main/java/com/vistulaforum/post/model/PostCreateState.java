package com.vistulaforum.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PostCreateState {

    OK(200, "OK"),
    POST_NOT_FOUND(204, "Post not found"),
    USER_NOT_FOUND(400, "User not found"),
    BODY_TEXT_EMPTY(400, "Body text cannot be empty or is to short"),
    USER_IS_NOT_AN_AUTHOR(403, "User is not an author of post");

    private final int code;
    private final String message;

}
