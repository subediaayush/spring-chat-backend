package com.thehedgelog.spring.demo.nats;

import com.thehedgelog.spring.demo.ChatMessage;
import com.thehedgelog.spring.demo.nats.serde.ChatMessageNatsMesssageSerDes;
import com.thehedgelog.spring.demo.nats.server.Publish;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope
@Component
public class ChatMessagePublisher {

    @Publish(subject = "chat", stream = "message", serde = ChatMessageNatsMesssageSerDes.class)
    public void publishChat(ChatMessage message) {}

}
