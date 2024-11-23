package com.vistulaforum.topic.controller;

import com.vistulaforum.result.Result;
import com.vistulaforum.topic.model.create.TopicHandleBodyDto;
import com.vistulaforum.topic.model.create.TopicHandleResult;
import com.vistulaforum.topic.model.create.TopicCreateState;
import com.vistulaforum.topic.model.dto.TopicDto;
import com.vistulaforum.topic.model.dto.TopicPreviewDto;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(path = "/topics")
public class TopicController {

    @Autowired
    private TopicDaoService topicDaoService;

    @Autowired
    private UserDaoService userDaoService;

    @PostMapping(path = "/create")
    public ResponseEntity<TopicHandleResult> createTopic(@AuthenticationPrincipal UserDetails userDetails,
                                                         @RequestBody TopicHandleBodyDto topicHandleBody) {
        Optional<UserDao> userDaoOptional = userDaoService.getUserDaoByLogin(userDetails.getUsername());
        if (userDaoOptional.isEmpty()) {
            return new ResponseEntity<>(new TopicHandleResult(new Result(TopicCreateState.USER_NOT_FOUND), null), HttpStatus.UNAUTHORIZED);
        }

        TopicHandleResult response = topicDaoService.createTopic(userDaoOptional.get(), topicHandleBody);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResult().getCode()));
    }

    @PutMapping(path = "/edit")
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

    @DeleteMapping(path = "/delete")
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

    @GetMapping(path = "/preview")
    public ResponseEntity<List<TopicPreviewDto>> getTopicsPreview() {
        List<TopicPreviewDto> response = topicDaoService.getTopicsPreview();
        if (response.isEmpty()) {
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<TopicDto> getTopicById(@PathVariable(name = "id") BigInteger topicId) {
        TopicDto response = topicDaoService.getTopicDtoById(topicId);
        if (Objects.equals(response, null)) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
