package io.github.jiangood.as.modules.flowable.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

@ConditionalOnClass(name = "org.flowable.engine.ProcessEngine")
@AutoConfiguration
public class FlowableAutoConfiguration {


}
