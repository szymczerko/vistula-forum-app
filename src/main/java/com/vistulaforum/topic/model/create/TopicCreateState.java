package com.vistulaforum.topic.model.create;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TopicCreateState {

    OK(200, "OK"),
    TOPIC_NOT_FOUND(204, "Topic not found"),
    USER_NOT_FOUND(400, "User not found"),
    TITLE_EMPTY(400, "Title cannot be empty or is to short"),
    BODY_TEXT_EMPTY(400, "Body text cannot be empty or is to short"),
    USER_IS_NOT_AN_AUTHOR(403, "User is not an author of post");

    private final int code;
    private final String message;

}
