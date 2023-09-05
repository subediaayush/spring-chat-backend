package com.thehedgelog.spring.demo;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
    private String from;
    private String to;
    private String message;
    private long timestamp;
    private String bucket;

    public static ChatMessageDto fromMessage(ChatMessage message) {
        return ChatMessageDto.builder().from(message.getMessage())
                .to(message.getReceiver())
                .message(message.getMessage())
                .bucket(message.getBucket())
                .timestamp(message.getTimestamp().toEpochMilli())
                .build();
    }

    public ChatMessage toMessage() {
        return ChatMessage.builder().sender(this.getFrom())
                .receiver(this.getTo())
                .message(this.getMessage())
                .bucket(this.bucket)
                .timestamp(Instant.ofEpochMilli(timestamp))
                .build();
    }
}
