package com.vistulaforum.topic.model.create;

import com.vistulaforum.result.Result;
import com.vistulaforum.topic.model.dto.TopicDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TopicHandleResult {

    private Result result;
    private TopicDto topic;

    public TopicHandleResult(Result result, TopicDto topic) {
        this.result = result;
        this.topic = topic;
    }

}
