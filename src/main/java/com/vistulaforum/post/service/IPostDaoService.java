package com.vistulaforum.post.service;

import com.vistulaforum.post.model.PostHandleBodyDto;
import com.vistulaforum.post.model.PostHandleResult;
import com.vistulaforum.post.model.dto.PostDto;
import com.vistulaforum.user.model.dao.UserDao;

import java.math.BigInteger;
import java.util.List;

public interface IPostDaoService {

    PostHandleResult createPost(UserDao userDao, PostHandleBodyDto postHandleBody);
    PostHandleResult editPost(UserDao userDao, PostHandleBodyDto postHandleBody, BigInteger postId);
    boolean deletePost(UserDao userDao, BigInteger postId);
    int getPostsCountByTopicId(BigInteger topicId);
    List<PostDto> getPostsDtoByTopic(BigInteger topicId);
    void deleteAllPostsByTopicId(BigInteger topicId);
}
