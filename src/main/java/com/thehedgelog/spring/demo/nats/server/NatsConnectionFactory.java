package com.thehedgelog.spring.demo.nats.server;

import io.nats.client.Connection;
import io.nats.client.JetStream;
import io.nats.client.Nats;
import io.nats.client.Options;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Scope
public class NatsConnectionFactory {

    @Bean
    @Scope
    public Connection natsConnection(NatsConfig config) throws IOException, InterruptedException {
        return Nats.connect(Options.builder()
                .server(config.getAddress())
                .userInfo(config.getUsername(), config.getPassword())
                .build());
    }

    @Bean
    @Scope
    public JetStream jetStream(Connection connection) throws IOException, InterruptedException {
        return connection.jetStream();
    }

}
