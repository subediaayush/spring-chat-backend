package com.thehedgelog.spring.demo.nats.server;


import com.thehedgelog.spring.demo.nats.serde.NatsMessageSerDes;
import io.nats.client.JetStreamSubscription;
import io.nats.client.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Getter
@RequiredArgsConstructor
public class JetStreamSubscriptionMessage<T> implements Runnable {

    private final String name;
    private final JetStreamSubscription subscription;
    private final int batchSize;
    private final long waitTime;
    private final Consumer<T> callback;
    private final NatsMessageSerDes<T> serde;

    private List<Message> messages;

    @Override
    public void run() {
        log.trace("Message cycle started - {}", name);
        try {
            do {
                log.trace("Checking for messages - {}", name);
                messages = subscription.fetch(batchSize, Duration.ofSeconds(waitTime));
                if (messages.size() > 0) {
                    log.debug("Received {} nats message {}, {}", messages.size(), subscription.getConsumerName(), name);
                }
                messages.stream().peek(message -> {
                    try {
                        T deserialized = serde.deserialize(message.getData());
                        callback.accept(deserialized);
                    } catch (Exception e) {
                        log.info("Error parsing message {}", message, e);
                    }
                }).forEach(Message::ack);
            } while (!messages.isEmpty());
        } catch (Exception e) {
            log.error("Error processing messages", e);
            // Prevent any exception so that subsequent executions are not blocked
        }
        log.trace("Message cycle stopped - {}", name);
    }
}
