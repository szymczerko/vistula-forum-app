package com.vistulaforum.topic.controller;

import com.vistulaforum.result.Result;
import com.vistulaforum.topic.model.create.TopicHandleBodyDto;
import com.vistulaforum.topic.model.create.TopicHandleResult;
import com.vistulaforum.topic.model.create.TopicCreateState;
import com.vistulaforum.topic.model.dto.TopicDto;
import com.vistulaforum.topic.service.TopicDaoService;
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
public class TopicController {

    @Autowired
    private TopicDaoService topicDaoService;

    @Autowired
    private UserDaoService userDaoService;

    @PostMapping(path = "/topic/create")
    public ResponseEntity<TopicHandleResult> createTopic(@AuthenticationPrincipal UserDetails userDetails,
                                                         @RequestBody TopicHandleBodyDto topicHandleBody) {
        Optional<UserDao> userDaoOptional = userDaoService.getUserDaoByLogin(userDetails.getUsername());
        if (userDaoOptional.isEmpty()) {
            return new ResponseEntity<>(new TopicHandleResult(new Result(TopicCreateState.USER_NOT_FOUND), null), HttpStatus.UNAUTHORIZED);
        }

        TopicHandleResult response = topicDaoService.createTopic(userDaoOptional.get(), topicHandleBody);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResult().getCode()));
    }

    @PutMapping(path = "/topic/edit")
    public ResponseEntity<TopicHandleResult> editTopic(@AuthenticationPrincipal UserDetails userDetails,
                                                       @RequestBody TopicHandleBodyDto topicHandleBody,
                                                       @RequestParam(name = "topic_id") BigInteger topicId) {
        Optional<UserDao> userDaoOptional = userDaoService.getUserDaoByLogin(userDetails.getUsername());
        if (userDaoOptional.isEmpty()) {
            return new ResponseEntity<>(new TopicHandleResult(new Result(TopicCreateState.USER_NOT_FOUND), null), HttpStatus.UNAUTHORIZED);
        }

        TopicHandleResult response = topicDaoService.editTopic(userDaoOptional.get(), topicHandleBody, topicId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResult().getCode()));
    }

    @DeleteMapping(path = "/topic/delete")
    public ResponseEntity<String> deleteTopic(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestParam(name = "topic_id") BigInteger topicId) {
        Optional<UserDao> userDaoOptional = userDaoService.getUserDaoByLogin(userDetails.getUsername());
        if (userDaoOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        boolean response = topicDaoService.deleteTopic(userDaoOptional.get(), topicId);
        if (!response) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
