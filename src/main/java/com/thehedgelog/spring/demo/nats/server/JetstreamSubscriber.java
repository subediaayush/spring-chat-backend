package com.thehedgelog.spring.demo.nats.server;

import com.thehedgelog.spring.demo.nats.serde.NatsMessageSerDes;
import io.nats.client.Connection;
import io.nats.client.JetStreamApiException;
import io.nats.client.JetStreamManagement;
import io.nats.client.api.RetentionPolicy;
import io.nats.client.api.StorageType;
import io.nats.client.api.StreamConfiguration;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Component
@Slf4j
public class JetstreamSubscriber implements ApplicationListener<ApplicationStartedEvent> {

    private final Connection nc;
    private final JetStreamConsumer consumer;
    private final Set<String> publisherStreams;

    public JetstreamSubscriber(Connection nc, JetStreamConsumer consumer) {
        this.nc = nc;
        this.consumer = consumer;
        this.publisherStreams = new ConcurrentSkipListSet<>();;
    }


    @SneakyThrows
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        for (Map.Entry<String, NatsListener> entry : event.getApplicationContext().getBeansOfType(NatsListener.class).entrySet()) {
            NatsListener value = entry.getValue();
            registerListener(event.getApplicationContext(), value);
        }
    }

    @SneakyThrows
    private void registerListener(ApplicationContext context, NatsListener natsListener) {
        for (Method method : natsListener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Accept.class)) {

                Accept accept = method.getAnnotation(Accept.class);

                String stream = accept.stream();
                String subject = accept.subject();

                String durable = accept.durable();
                long pullWaitTime = accept.pullWaitTime();
                int batchSize = accept.batchSize();
                long ackWaitTime = accept.ackWaitTime();
                long frequency = accept.delay();

                Object serde;
                try {
                    serde = context.getBean(accept.serde().getSimpleName());
                    log.info("Obtained mapper bean");
                } catch (NoSuchBeanDefinitionException e) {
                    serde = accept.serde().getDeclaredConstructor().newInstance();
                    log.info("Obtained mapper from constructor");
                }

                createJetStream(stream, subject);
                consumer.registerConsumer(subject, stream, durable, ackWaitTime);
                consumer.subscribe(subject, stream, durable, batchSize, pullWaitTime, frequency, s -> {
                    try {
                        method.invoke(natsListener, s);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }, (NatsMessageSerDes<?>) serde);
            }
        }
    }

    public void createJetStream(String name, String subject) throws IOException, JetStreamApiException {
        if (publisherStreams.add(name)) {
            JetStreamManagement jsm = nc.jetStreamManagement();
            StreamConfiguration configuration = StreamConfiguration.builder()
                    .name(name)
                    .storageType(StorageType.File)
                    .subjects(subject)
                    .retentionPolicy(RetentionPolicy.Interest)
                    .build();

            jsm.addStream(configuration);
        }
    }

}
