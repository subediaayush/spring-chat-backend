package com.thehedgelog.spring.demo;

import lombok.*;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("chat_message")
public class ChatMessage {
    private String sender;
    private String receiver;
    private String message;
    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    public String bucket;
    @PrimaryKeyColumn(ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    public Instant timestamp;

    public static String getBucket(String from, String to) {
        if (from.compareTo(to) > 0) {
            return from + to;
        } else {
            return to + from;
        }
    }

    public void assignBucket() {
        this.bucket = getBucket(sender, receiver);
    }

//    @EqualsAndHashCode
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Data
//    @PrimaryKeyClass
//    public static class ChatId implements Serializable {
//    }
}
