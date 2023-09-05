package com.thehedgelog.spring.demo;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Component
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public List<ChatMessage> listMessages(String from, String to) {
        var bucket = ChatMessage.getBucket(from, to);
        return chatRepository.findAllByBucket(bucket);
    }

//    public void broadcastMessage(ChatMessage message) {
////        String userId = message.getTo();
////        String sender = message.getFrom();
//
//        socket.sendMessage(ChatMessageDto.fromMessage(message));
//    }

}
