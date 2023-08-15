package com.thehedgelog.spring.demo.nats.server;

import com.thehedgelog.spring.demo.nats.serde.NatsMessageSerDes;
import io.nats.client.Connection;
import io.nats.client.JetStream;
import io.nats.client.JetStreamApiException;
import io.nats.client.PullSubscribeOptions;
import io.nats.client.api.AckPolicy;
import io.nats.client.api.ConsumerConfiguration;
import io.nats.client.api.DeliverPolicy;
import io.nats.client.api.ReplayPolicy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.function.Consumer;

@Slf4j
@Scope
@Component
public class JetStreamConsumer {

    private final Connection natsConnection;
    private final TaskScheduler scheduler;
    private final JetStream jetStream;

    public JetStreamConsumer(Connection natsConnection, TaskScheduler scheduler, JetStream jetStream) {
        this.natsConnection = natsConnection;
        this.scheduler = scheduler;
        this.jetStream = jetStream;
    }

    public void subscribe(String subject, String stream, String durable, int batchSize, long waitTime, long frequency, Consumer<?> handler, NatsMessageSerDes<?> serde) throws JetStreamApiException, IOException {
        var subscription = jetStream.subscribe(subject, PullSubscribeOptions.builder().stream(stream).durable(durable).name(durable).build());

        scheduler.scheduleWithFixedDelay(
                new JetStreamSubscriptionMessage(subject, subscription, batchSize, waitTime, handler, serde),
                Duration.ofSeconds(frequency)
        );

        log.debug("Subscribed to {}-{}-{}-{}-{}-{}", subject, stream, durable, batchSize, waitTime, frequency);
    }

    public void registerConsumer(String subject, String stream, String durable, long ackWaitTime) throws IOException, JetStreamApiException {
        var config = ConsumerConfiguration.builder()
                .ackPolicy(AckPolicy.Explicit)
                .deliverPolicy(DeliverPolicy.All)
                .replayPolicy(ReplayPolicy.Instant)
                .ackWait(Duration.ofSeconds(ackWaitTime))
                .durable(durable)
                .deliverSubject(null)
                .filterSubject(subject)
                .build();

        natsConnection.jetStreamManagement().addOrUpdateConsumer(stream, config);
        log.debug("Registered consumer to {}-{}-{}-{}", subject, stream, durable, ackWaitTime);
    }
}
