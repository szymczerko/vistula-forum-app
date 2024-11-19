package com.vistulaforum.post.controller;

import com.vistulaforum.post.model.PostHandleBodyDto;
import com.vistulaforum.post.model.PostHandleResult;
import com.vistulaforum.post.service.PostDaoService;
import com.vistulaforum.result.Result;
import com.vistulaforum.topic.model.create.TopicCreateState;
import com.vistulaforum.topic.model.create.TopicHandleResult;
import com.vistulaforum.user.model.dao.UserDao;
import com.vistulaforum.user.service.UserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class PostController {

    @Autowired
    private PostDaoService postDaoService;

    @Autowired
    private UserDaoService userDaoService;


    @PostMapping(path = "/post/create")
    public ResponseEntity<PostHandleResult> createPost(@AuthenticationPrincipal UserDetails userDetails,
                                                       @RequestBody PostHandleBodyDto postHandleBody) {
        Optional<UserDao> userDaoOptional = userDaoService.getUserDaoByLogin(userDetails.getUsername());
        if (userDaoOptional.isEmpty()) {
            return new ResponseEntity<>(new PostHandleResult(new Result(TopicCreateState.USER_NOT_FOUND), null), HttpStatus.UNAUTHORIZED);
        }
        

    }

}
