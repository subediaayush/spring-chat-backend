package com.thehedgelog.spring.demo;

import com.thehedgelog.spring.demo.nats.ChatMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class WSController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessagePublisher publisher;

    public WSController(SimpMessagingTemplate simpMessagingTemplate, ChatMessagePublisher publisher) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.publisher = publisher;
    }

    @MessageMapping("/chat/{senderId}")
    public void onMessageReceived(@DestinationVariable String senderId, @Payload ChatMessageDto message) {
        log.info("Message received {} form {}", message, senderId);
        broadcast(message.getTo(), message);
        publisher.publishChat(message.toMessage());
    }

    public void broadcast(@Header String receiverId, @Payload ChatMessageDto message) {
        String destination = "/topic/%s/messages".formatted(receiverId);
        simpMessagingTemplate.convertAndSend(destination, message);
        log.info("Sent message {} to {}", message, destination);
    }

}
