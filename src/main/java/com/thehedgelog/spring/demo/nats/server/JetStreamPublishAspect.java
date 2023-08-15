package com.thehedgelog.spring.demo.nats.server;


import com.thehedgelog.spring.demo.nats.serde.NatsMessageSerDes;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Arrays;

@Scope
@Component
@Slf4j
public class JetStreamPublishAspect implements MethodInterceptor {

    private final ApplicationContext context;
    private final JetStreamPublisher publisher;

    public JetStreamPublishAspect(ApplicationContext context, JetStreamPublisher publisher) {
        this.context = context;
        this.publisher = publisher;
    }

    public boolean doesIntercept(Method method) {
        return method.isAnnotationPresent(Publish.class);
    }

//    @Around("@annotation(Publish)")
//    public Object publishMessage(@Nonnull ProceedingJoinPoint joinPoint) throws Throwable {
//        if (joinPoint.getKind().equals(METHOD_EXECUTION)) {
//        }
//        if (doesIntercept(method)) {
//            var annotation = method.getAnnotation(Publish.class);
//
//            var subject = annotation.subject();
//            var serde = (NatsMessageSerDes) annotation.serde().getDeclaredConstructor().newInstance();
//
//            var message = Arrays.stream(invocation.getArguments()).findFirst().orElseThrow();
//
//            publisher.publish(subject, serde.serialize(message));
//
//            return null;
//        }
//
//        return joinPoint.proceed();
//    }

    @Nullable
    @Override
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        var method = invocation.getMethod();

        if (doesIntercept(method)) {
            var annotation = method.getAnnotation(Publish.class);

            var subject = annotation.subject();
            Object serde;
            try {
                serde = context.getBean(annotation.serde().getSimpleName());
                log.info("Obtained mapper bean");
            } catch (NoSuchBeanDefinitionException e) {
                serde = annotation.serde().getDeclaredConstructor().newInstance();
                log.info("Obtained mapper from constructor");
            }

            var message = Arrays.stream(invocation.getArguments()).findFirst().orElseThrow();

            publisher.publish(subject, ((NatsMessageSerDes) serde).serialize(message));

            return null;
        }

        return invocation.proceed();
    }
}
