package com.thehedgelog.spring.demo.nats.serde;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component("DefaultNatsMesssageSerDes")
public class DefaultNatsMesssageSerDes implements NatsMessageSerDes<String> {

    @Override
    public Class<String> getClazz() {
        return String.class;
    }

    @Override
    public String deserialize(byte[] data) {
        return new String(data, StandardCharsets.UTF_8);
    }

    @Override
    public byte[] serialize(String data) {
        return data.getBytes(StandardCharsets.UTF_8);
    }
}
