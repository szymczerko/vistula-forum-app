package com.vistulaforum.topic.model.dto;

import com.vistulaforum.post.model.dto.PostDto;
import com.vistulaforum.user.model.dto.UserDto;
import lombok.*;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TopicDto {

    private BigInteger id;
    private String title;
    private String text;
    private Timestamp createdAtUtc;
    private Timestamp editedAtUtc;
    private UserDto author;
    private List<PostDto> posts;

}
