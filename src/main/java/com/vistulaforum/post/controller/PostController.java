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
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Optional;

@RestController
@RequestMapping(path = "/posts")
public class PostController {

    @Autowired
    private PostDaoService postDaoService;

    @Autowired
    private UserDaoService userDaoService;


    @PostMapping(path = "/create")
    public ResponseEntity<PostHandleResult> createPost(@AuthenticationPrincipal UserDetails userDetails,
                                                       @RequestBody PostHandleBodyDto postHandleBody) {
        Optional<UserDao> userDaoOptional = userDaoService.getUserDaoByLogin(userDetails.getUsername());
        if (userDaoOptional.isEmpty()) {
            return new ResponseEntity<>(new PostHandleResult(new Result(TopicCreateState.USER_NOT_FOUND), null), HttpStatus.UNAUTHORIZED);
        }

        PostHandleResult result = postDaoService.createPost(userDaoOptional.get(), postHandleBody);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getResult().getCode()));
    }

    @PutMapping(path = "/edit")
    public ResponseEntity<PostHandleResult> editPost(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestBody PostHandleBodyDto postHandleBody,
                                                     @RequestParam(name = "post_id") BigInteger postId) {
        Optional<UserDao> userDaoOptional = userDaoService.getUserDaoByLogin(userDetails.getUsername());
        if (userDaoOptional.isEmpty()) {
            return new ResponseEntity<>(new PostHandleResult(new Result(TopicCreateState.USER_NOT_FOUND), null), HttpStatus.UNAUTHORIZED);
        }
        PostHandleResult result = postDaoService.editPost(userDaoOptional.get(), postHandleBody, postId);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getResult().getCode()));
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<?> deletePost(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestParam(name = "post_id") BigInteger postId) {
        Optional<UserDao> userDaoOptional = userDaoService.getUserDaoByLogin(userDetails.getUsername());
        if (userDaoOptional.isEmpty()) {
            return new ResponseEntity<>(new PostHandleResult(new Result(TopicCreateState.USER_NOT_FOUND), null), HttpStatus.UNAUTHORIZED);
        }

        boolean response = postDaoService.deletePost(userDaoOptional.get(), postId);
        if (!response) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
