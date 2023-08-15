package com.thehedgelog.spring.demo.nats.serde;

public interface NatsMessageSerDes<T> {
    Class<T> getClazz();
    T deserialize(byte[] data) ;
    byte[] serialize(T data);
}
