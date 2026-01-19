package io.github.jiangood.as.modules.flowable.dto.request;

import io.github.jiangood.as.modules.flowable.dto.TaskHandleType;
import lombok.Data;

@Data
public class HandleTaskRequest {

    TaskHandleType result;
    String taskId;
    String comment;
}
