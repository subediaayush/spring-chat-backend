package com.thehedgelog.spring.demo.nats.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thehedgelog.spring.demo.ChatMessage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component("ChatMessageNatsMesssageSerDes")
public class ChatMessageNatsMesssageSerDes implements NatsMessageSerDes<ChatMessage> {

    private ObjectMapper mapper;

    @Override
    public Class<ChatMessage> getClazz() {
        return ChatMessage.class;
    }

    @SneakyThrows
    @Override
    public ChatMessage deserialize(byte[] data) {
        return getMapper().readValue(data, getClazz());
    }

    private ObjectMapper getMapper() {
        if (mapper == null)
            mapper = Jackson2ObjectMapperBuilder.json().build();

        return mapper;
    }

    @SneakyThrows
    @Override
    public byte[] serialize(ChatMessage data) {
        return getMapper().writeValueAsBytes(data);
    }
}
