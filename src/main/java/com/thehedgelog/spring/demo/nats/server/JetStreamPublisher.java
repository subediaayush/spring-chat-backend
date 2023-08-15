package com.thehedgelog.spring.demo.nats.server;


import io.nats.client.Connection;
import io.nats.client.impl.NatsMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Scope
@Slf4j
@Component
public class JetStreamPublisher {

    private final Connection nc;

    public JetStreamPublisher(Connection nc) {
        this.nc = nc;
    }

    public void publish(String subject, byte[] message) throws IOException {
        log.debug("Publishing to subject {}", subject);
        this.nc.jetStream().publishAsync(NatsMessage.builder().subject(subject).data(message).build())
                .whenComplete((publishAck, throwable) -> {
                    if (throwable == null)
                        log.debug("Published to subject {}, {}", subject, publishAck.getStream());
                    else
                        log.error("Error publishing to subject {}", subject, throwable);
                });
    }
}
