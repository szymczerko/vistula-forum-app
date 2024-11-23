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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
    @Transactional
    public PostHandleResult createPost(UserDao userDao, PostHandleBodyDto postHandleBody) {
        Optional<TopicDao> topicDaoOptional = topicDaoService.getTopicDaoById(postHandleBody.getTopicId());
        if (topicDaoOptional.isEmpty()) {
            return new PostHandleResult(new Result(PostCreateState.POST_NOT_FOUND), null);
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
    @Transactional
    public PostHandleResult editPost(UserDao userDao, PostHandleBodyDto postHandleBody, BigInteger postId) {
        Optional<PostDao> postDaoOptional = postDaoRepository.getPostDaoById(postId);
        if (postDaoOptional.isEmpty()) {
            return new PostHandleResult(new Result(PostCreateState.POST_NOT_FOUND), null);
        }
        PostDao postDao = postDaoOptional.get();
        if (this.isUserAnAuthorOfPost(postDao, userDao)) {
            return new PostHandleResult(new Result(PostCreateState.USER_IS_NOT_AN_AUTHOR), null);
        }

        if (postHandleBody.getText().length() < MIN_TOPIC_BODY_LENGTH) {
            return new PostHandleResult(new Result(PostCreateState.BODY_TEXT_EMPTY), null);
        }

        postDao.setText(postHandleBody.getText());
        postDao.setEditedAt(Timestamp.from(Instant.now()));
        postDaoRepository.save(postDao);
        return new PostHandleResult(new Result(PostCreateState.OK), this.buildPostDtoFromDao(postDao, userDao));
    }

    @Override
    @Transactional
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

    @Override
    public int getPostsCountByTopicId(BigInteger topicId) {
        return postDaoRepository.getPostsCountByTopicId(topicId);
    }

    @Override
    public List<PostDto> getPostsDtoByTopic(BigInteger topicId) {
        List<PostDao> postDaoList = postDaoRepository.getPostsDaoByTopicId(topicId);
        if (postDaoList.isEmpty()) {
            return new ArrayList<>();
        }
        List<PostDto> postDtoList = new ArrayList<>();
        for (PostDao postDao : postDaoList) {
            postDtoList.add(this.buildPostDtoFromDao(postDao));
        }
        return postDtoList;
    }

    @Override
    @Transactional
    public void deleteAllPostsByTopicId(BigInteger topicId) {
        List<PostDao> postDaoList = postDaoRepository.getPostsDaoByTopicId(topicId);
        if (postDaoList.isEmpty()) {
            return;
        }
        postDaoRepository.deleteAll(postDaoList);
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

    private PostDto buildPostDtoFromDao(PostDao postDao) {
        return PostDto.builder()
                .id(postDao.getId())
                .text(postDao.getText())
                .createdAtUtc(postDao.getCreatedAt())
                .editedAtUtc(postDao.getEditedAt())
                .author(userDaoService.getUserDto(postDao.getAuthor()))
                .build();
    }

     private boolean isUserAnAuthorOfPost(PostDao post, UserDao user) {
        return Objects.equals(post.getAuthor().getId(), user.getId());
    }
}
