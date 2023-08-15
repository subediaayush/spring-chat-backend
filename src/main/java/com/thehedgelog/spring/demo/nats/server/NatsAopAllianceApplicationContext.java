package com.thehedgelog.spring.demo.nats.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.thehedgelog.spring.demo")
@Slf4j
public class NatsAopAllianceApplicationContext {

    private final ApplicationContext context;
    private final JetStreamPublisher publisher;

    public NatsAopAllianceApplicationContext(ApplicationContext context, JetStreamPublisher publisher) {
        this.context = context;
        this.publisher = publisher;
    }

    @Bean
    public Advisor advisor() {
        log.info("Registering js publisher");
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("@annotation(com.thehedgelog.spring.demo.nats.server.Publish)");
        return new DefaultPointcutAdvisor(pointcut, new JetStreamPublishAspect(context, publisher));
    }

}
