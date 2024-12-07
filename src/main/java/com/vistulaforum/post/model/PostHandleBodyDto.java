package com.vistulaforum.post.model;

import lombok.*;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostHandleBodyDto {

    private BigInteger topicId;
    private String text;

}
