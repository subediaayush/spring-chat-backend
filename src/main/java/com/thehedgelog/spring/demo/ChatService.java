package com.thehedgelog.spring.demo;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Component
public class ChatService {

    private final WSController socket;

    public ChatService(WSController socket) {
        this.socket = socket;
    }

//    public void broadcastMessage(ChatMessage message) {
////        String userId = message.getTo();
////        String sender = message.getFrom();
//
//        socket.sendMessage(ChatMessageDto.fromMessage(message));
//    }

}
