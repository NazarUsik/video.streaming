package com.test.task.videostream.service.message;

public interface MessageService {
    <T> void sendMessage(String topic, T message);
}
