package io.github.jiangood.as.modules.flowable.config;

import io.github.jiangood.as.common.tools.SpringTool;
import io.github.jiangood.as.modules.flowable.config.meta.ProcessListener;
import io.github.jiangood.as.modules.flowable.config.meta.ProcessMeta;
import io.github.jiangood.as.modules.flowable.core.FlowableEventType;
import io.github.jiangood.as.modules.flowable.service.ProcessMetaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.delegate.event.impl.FlowableProcessEventImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class GlobalProcessListener implements FlowableEventListener {


    private ProcessMetaService processMetaService;


    @Override
    public void onEvent(FlowableEvent flowableEvent) {
        if (!(flowableEvent instanceof FlowableProcessEventImpl event)) {
            return;
        }

        log.trace("流程事件 {} ", flowableEvent);

        String typeName = flowableEvent.getType().name();
        long count = Arrays.stream(FlowableEventType.values()).filter(t -> t.name().equals(typeName)).count();
        if (count == 0) {
            return;
        }

        FlowableEventType eventType = FlowableEventType.findByName(typeName);
        if (eventType == null) {
            return;
        }

        String instanceId = event.getProcessInstanceId();
        ExecutionEntityImpl execution = (ExecutionEntityImpl) event.getExecution();
        String definitionKey = execution.getProcessDefinitionKey();


        log.info("流程事件 {} {}", definitionKey, event.getType());

        Map<String, Object> variables = execution.getVariables();
        // String initiator = (String) variables.get("INITIATOR");
        String businessKey = execution.getBusinessKey();
        String initiator = execution.getStartUserId();


        // 触发
        ProcessMeta meta = processMetaService.findOne(definitionKey);
        if (meta != null) {
            Class<? extends ProcessListener> listener = meta.getListener();
            if (listener != null) {
                ProcessListener bean = SpringTool.getBean(listener);
                if (bean != null) {
                    bean.onProcessEvent(eventType, initiator, businessKey, variables);
                }
            }
        }


    }

    public static void main(String[] args) {
        FlowableEventType eventType = FlowableEventType.valueOf("a");
        System.out.println(eventType);
    }


    @Override
    public boolean isFailOnException() {
        return true;
    }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return false;
    }

    @Override
    public String getOnTransaction() {
        return null;
    }


}
