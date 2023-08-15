package com.thehedgelog.spring.demo.nats.server;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "nats")
@Getter
@Setter
@Scope
@Component
public class NatsConfig {

    public String address;
    public String username;
    public String password;



}
