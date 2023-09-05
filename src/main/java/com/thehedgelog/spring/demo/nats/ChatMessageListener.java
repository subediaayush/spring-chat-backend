package com.thehedgelog.spring.demo.nats;

import com.thehedgelog.spring.demo.ChatMessage;
import com.thehedgelog.spring.demo.ChatRepository;
import com.thehedgelog.spring.demo.nats.serde.ChatMessageNatsMesssageSerDes;
import com.thehedgelog.spring.demo.nats.server.Accept;
import com.thehedgelog.spring.demo.nats.server.NatsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.data.cassandra.core.InsertOptions;
import org.springframework.stereotype.Component;

@Scope
@Component
@Slf4j
public class ChatMessageListener implements NatsListener {

    private final ChatRepository chatRepository;

    public ChatMessageListener(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Accept(subject = "chat", stream = "message", durable = "c_m", serde = ChatMessageNatsMesssageSerDes.class)
    public void onMessageReceived(ChatMessage message) {
        message.assignBucket();
        this.chatRepository.insert(message);
    }

}
