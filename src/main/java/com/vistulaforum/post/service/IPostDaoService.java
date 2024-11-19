package com.vistulaforum.post.service;

import com.vistulaforum.post.model.PostHandleBodyDto;
import com.vistulaforum.post.model.PostHandleResult;
import com.vistulaforum.user.model.dao.UserDao;

import java.math.BigInteger;

public interface IPostDaoService {

    PostHandleResult createPost(UserDao userDao, PostHandleBodyDto postHandleBody);
    boolean deletePost(UserDao userDao, BigInteger postId);

}
