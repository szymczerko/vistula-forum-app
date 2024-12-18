package com.vistulaforum.topic.service;

import com.vistulaforum.post.service.PostDaoService;
import com.vistulaforum.result.Result;
import com.vistulaforum.topic.model.create.TopicHandleBodyDto;
import com.vistulaforum.topic.model.create.TopicHandleResult;
import com.vistulaforum.topic.model.create.TopicCreateState;
import com.vistulaforum.topic.model.dao.TopicDao;
import com.vistulaforum.topic.model.dto.TopicDto;
import com.vistulaforum.topic.model.dto.TopicPreviewDto;
import com.vistulaforum.topic.repository.ITopicDaoRepository;
import com.vistulaforum.user.model.dao.UserDao;
import com.vistulaforum.user.model.login.LoginUserState;
import com.vistulaforum.user.service.UserDaoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TopicDaoService implements ITopicDaoService {

    @Autowired
    private ITopicDaoRepository topicDaoRepository;

    @Autowired
    private UserDaoService userDaoService;

    @Autowired
    @Lazy
    private PostDaoService postDaoService;

    private final int MIN_TOPIC_TITLE_LENGTH = 5;
    private final int MIN_TOPIC_BODY_LENGTH = 10;

    @Override
    @Transactional
    public TopicHandleResult createTopic(UserDao userDao, TopicHandleBodyDto topicHandleBody) {
        TopicHandleResult topicCreateResult = this.checkIfTopicBodyIsValid(topicHandleBody);
        if (topicCreateResult != null) {
            return topicCreateResult;
        }

        TopicDao topicDao = TopicDao.builder()
                .title(topicHandleBody.getTitle())
                .text(topicHandleBody.getText())
                .author(userDao)
                .createdAt(Timestamp.from(Instant.now()))
                .build();
        topicDao = topicDaoRepository.save(topicDao);

        return new TopicHandleResult(new Result(LoginUserState.OK), this.buildTopicDtoFromDao(topicDao, userDao));
    }

    @Override
    @Transactional
    public TopicHandleResult editTopic(UserDao userDao, TopicHandleBodyDto topicHandleBody, BigInteger topicId) {
        Optional<TopicDao> topicDaoOptional = topicDaoRepository.getTopicDaoById(topicId);
        if (topicDaoOptional.isEmpty()) {
            return new TopicHandleResult(new Result(TopicCreateState.TOPIC_NOT_FOUND), null);
        }

        TopicDao topicDao = topicDaoOptional.get();
        if (!this.isUserAnAuthorOfTopic(topicDao, userDao)) {
            return new TopicHandleResult(new Result(TopicCreateState.USER_IS_NOT_AN_AUTHOR), null);
        }

        TopicHandleResult topicCreateResult = this.checkIfTopicBodyIsValid(topicHandleBody);
        if (topicCreateResult != null) {
            return topicCreateResult;
        }

        topicDao.setTitle(topicHandleBody.getTitle());
        topicDao.setText(topicHandleBody.getText());
        topicDao.setEditedAt(Timestamp.from(Instant.now()));
        topicDao = topicDaoRepository.save(topicDao);
        return new TopicHandleResult(new Result(TopicCreateState.OK), this.buildTopicDtoFromDao(topicDao, userDao));
    }

    @Override
    @Transactional
    public boolean deleteTopic(UserDao userDao, BigInteger topicId) {
        Optional<TopicDao> topicDaoOptional = topicDaoRepository.getTopicDaoById(topicId);
        if (topicDaoOptional.isEmpty()) {
            return false;
        }
        TopicDao topicDao = topicDaoOptional.get();
        if (this.isUserAnAuthorOfTopic(topicDao, userDao)) {
            postDaoService.deleteAllPostsByTopicId(topicId);
            topicDaoRepository.delete(topicDao);
            return true;
        }
        return false;
    }

    @Override
    public Optional<TopicDao> getTopicDaoById(BigInteger topicId) {
        return topicDaoRepository.getTopicDaoById(topicId);
    }

    @Override
    public TopicDto getTopicDtoById(BigInteger topicId) {
        Optional<TopicDao> topicDaoOptional = topicDaoRepository.getTopicDaoById(topicId);
        return topicDaoOptional.map(this::buildTopicDtoFromDao).orElse(null);
    }

    @Override
    public List<TopicPreviewDto> getTopicsPreview() {
        List<TopicDao> topicDaoList = topicDaoRepository.findAll();
        if (topicDaoList.isEmpty()) {
            return new ArrayList<>();
        }
        List<TopicPreviewDto> topicPreviewDtoList = new ArrayList<>();
        for (TopicDao topicDao : topicDaoList) {
            topicPreviewDtoList.add(this.buildTopicPreviewDtoFromDao(topicDao));
        }
        return topicPreviewDtoList;
    }

    private TopicHandleResult checkIfTopicBodyIsValid(TopicHandleBodyDto topicHandleBody) {
        if (topicHandleBody.getTitle().isEmpty() || topicHandleBody.getTitle().length() < MIN_TOPIC_TITLE_LENGTH ) {
            return new TopicHandleResult(new Result(TopicCreateState.TITLE_EMPTY), null);
        }

        if (topicHandleBody.getText().isEmpty() || topicHandleBody.getText().length() < MIN_TOPIC_BODY_LENGTH) {
            return new TopicHandleResult(new Result(TopicCreateState.BODY_TEXT_EMPTY), null);
        }

        return null;
    }

    private boolean isUserAnAuthorOfTopic(TopicDao topic, UserDao user) {
        return Objects.equals(topic.getAuthor().getId(), user.getId());
    }

    private TopicDto buildTopicDtoFromDao(TopicDao topicDao, UserDao userDao) {
        return TopicDto.builder()
                .id(topicDao.getId())
                .title(topicDao.getTitle())
                .text(topicDao.getText())
                .createdAtUtc(topicDao.getCreatedAt())
                .editedAtUtc(topicDao.getEditedAt())
                .author(userDaoService.getUserDto(userDao))
                .posts(new ArrayList<>())
                .build();
    }


    private TopicDto buildTopicDtoFromDao(TopicDao topicDao) {
        return TopicDto.builder()
                .id(topicDao.getId())
                .title(topicDao.getTitle())
                .text(topicDao.getText())
                .createdAtUtc(topicDao.getCreatedAt())
                .editedAtUtc(topicDao.getEditedAt())
                .author(userDaoService.getUserDto(topicDao.getAuthor()))
                .posts(postDaoService.getPostsDtoByTopic(topicDao.getId()))
                .build();
    }

    private TopicPreviewDto buildTopicPreviewDtoFromDao(TopicDao topicDao) {
        return TopicPreviewDto.builder()
                .id(topicDao.getId())
                .title(topicDao.getTitle())
                .createdAtUtc(topicDao.getCreatedAt())
                .author(userDaoService.getUserDto(topicDao.getAuthor()))
                .postsCount(postDaoService.getPostsCountByTopicId(topicDao.getId()))
                .build();
    }
}
