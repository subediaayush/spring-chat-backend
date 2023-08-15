package com.thehedgelog.spring.demo.nats.server;

import com.thehedgelog.spring.demo.nats.serde.DefaultNatsMesssageSerDes;
import com.thehedgelog.spring.demo.nats.serde.NatsMessageSerDes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Publish {
    String subject();
    String stream();
    Class<? extends NatsMessageSerDes<?>> serde() default DefaultNatsMesssageSerDes.class;
}
