package com.vistulaforum.post.model;

import com.vistulaforum.post.model.dto.PostDto;
import com.vistulaforum.result.Result;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostHandleResult {

    private Result result;
    private PostDto post;

    public PostHandleResult(Result result, PostDto post) {
        this.result = result;
        this.post = post;
    }

}
