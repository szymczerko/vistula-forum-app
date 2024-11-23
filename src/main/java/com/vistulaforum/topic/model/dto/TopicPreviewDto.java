package com.vistulaforum.topic.model.dto;

import com.vistulaforum.user.model.dto.UserDto;
import lombok.*;
import java.math.BigInteger;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TopicPreviewDto {

    private BigInteger id;
    private String title;
    private Timestamp createdAtUtc;
    private UserDto author;
    private int postsCount;

}
