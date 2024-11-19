package com.vistulaforum.post.model.dao;

import com.vistulaforum.topic.model.dao.TopicDao;
import com.vistulaforum.user.model.dao.UserDao;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "posts")
public class PostDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private BigInteger id;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDao author;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private TopicDao topic;

    @Column(name = "created_at_utc")
    private Timestamp createdAt;

    @Column(name = "edited_at_utc")
    private Timestamp editedAt;

}
