package com.vistulaforum.topic.model.dao;

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
@Table(name = "topics")
public class TopicDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private BigInteger id;

    @Column(name = "title")
    private String title;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDao author;

    @Column(name = "created_at_utc")
    private Timestamp createdAt;

    @Column(name = "edited_at_utc")
    private Timestamp editedAt;

}
