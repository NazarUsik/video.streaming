package com.test.task.videostream.service.message;

import com.test.task.videostream.config.kafka.KafkaTopicConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageManager {
    private final KafkaTopicConfig topicConfig;
    private final MessageService messageService;

    public <T> void sendMessage(T message) {
        messageService.sendMessage(topicConfig.getTopic(), message);
    }
}
