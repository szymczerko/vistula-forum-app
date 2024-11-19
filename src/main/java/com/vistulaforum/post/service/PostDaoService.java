package com.vistulaforum.post.service;

import com.vistulaforum.post.model.PostCreateState;
import com.vistulaforum.post.model.PostHandleBodyDto;
import com.vistulaforum.post.model.PostHandleResult;
import com.vistulaforum.post.model.dao.PostDao;
import com.vistulaforum.post.model.dto.PostDto;
import com.vistulaforum.post.repository.IPostDaoRepository;
import com.vistulaforum.result.Result;
import com.vistulaforum.topic.model.dao.TopicDao;
import com.vistulaforum.topic.service.TopicDaoService;
import com.vistulaforum.user.model.dao.UserDao;
import com.vistulaforum.user.service.UserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
public class PostDaoService implements IPostDaoService {

    @Autowired
    private IPostDaoRepository postDaoRepository;

    @Autowired
    private TopicDaoService topicDaoService;

    @Autowired
    private UserDaoService userDaoService;

    private final int MIN_TOPIC_BODY_LENGTH = 3;

    @Override
    public PostHandleResult createPost(UserDao userDao, PostHandleBodyDto postHandleBody) {
        Optional<TopicDao> topicDaoOptional = topicDaoService.getTopicDao(postHandleBody.getTopicId());
        if (topicDaoOptional.isEmpty()) {
            return new PostHandleResult(new Result(PostCreateState.TOPIC_NOT_FOUND), null);
        }

        if (postHandleBody.getText().length() < MIN_TOPIC_BODY_LENGTH) {
            return new PostHandleResult(new Result(PostCreateState.BODY_TEXT_EMPTY), null);
        }

        TopicDao topicDao = topicDaoOptional.get();

        PostDao postDao = PostDao.builder()
                .text(postHandleBody.getText())
                .author(userDao)
                .topic(topicDao)
                .createdAt(Timestamp.from(Instant.now()))
                .build();
        postDao = postDaoRepository.save(postDao);
        return new PostHandleResult(new Result(PostCreateState.OK), this.buildPostDtoFromDao(postDao, userDao));
    }

    @Override
    public boolean deletePost(UserDao userDao, BigInteger postId) {
        Optional<PostDao> postDaoOptional = postDaoRepository.getPostDaoById(postId);
        if (postDaoOptional.isEmpty()) {
            return false;
        }
        PostDao postDao = postDaoOptional.get();
        if (this.isUserAnAuthorOfPost(postDao, userDao)) {
            postDaoRepository.delete(postDao);
            return true;
        }
        return false;
    }

    private PostDto buildPostDtoFromDao(PostDao postDao, UserDao userDao) {
        return PostDto.builder()
                .id(postDao.getId())
                .text(postDao.getText())
                .createdAtUtc(postDao.getCreatedAt())
                .editedAtUtc(postDao.getEditedAt())
                .author(userDaoService.getUserDto(userDao))
                .build();
    }

     private boolean isUserAnAuthorOfPost(PostDao post, UserDao user) {
        return Objects.equals(post.getAuthor().getId(), user.getId());
    }
}
