package com.vistulaforum.topic.service;

import com.vistulaforum.topic.model.create.TopicHandleBodyDto;
import com.vistulaforum.topic.model.create.TopicHandleResult;
import com.vistulaforum.topic.model.dao.TopicDao;
import com.vistulaforum.user.model.dao.UserDao;
import java.math.BigInteger;
import java.util.Optional;

public interface ITopicDaoService {


    TopicHandleResult createTopic(UserDao userDao, TopicHandleBodyDto topicHandleBody);
    TopicHandleResult editTopic(UserDao userDao, TopicHandleBodyDto topicHandleBody, BigInteger topicId);
    boolean deleteTopic(UserDao userDao, BigInteger topicId);
    Optional<TopicDao> getTopicDao(BigInteger topicId);

}
