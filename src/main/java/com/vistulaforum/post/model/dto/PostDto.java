package com.vistulaforum.post.model.dto;

import com.vistulaforum.user.model.dto.UserDto;
import lombok.*;
import java.math.BigInteger;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PostDto {

    private BigInteger id;
    private String text;
    private Timestamp createdAtUtc;
    private Timestamp editedAtUtc;
    private UserDto author;

}
