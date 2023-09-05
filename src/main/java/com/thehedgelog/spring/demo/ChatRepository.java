package com.thehedgelog.spring.demo;

import org.springframework.data.cassandra.core.mapping.BasicMapId;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.cassandra.repository.MapIdCassandraRepository;

import java.util.List;

public interface ChatRepository extends MapIdCassandraRepository<ChatMessage> {

    default List<ChatMessage> findAllByBucket(String bucket) {
        return findAllById(List.of(BasicMapId.id("bucket", bucket)));
    }

//    List<ChatMessage> findAllById(MapId bucket);

}
