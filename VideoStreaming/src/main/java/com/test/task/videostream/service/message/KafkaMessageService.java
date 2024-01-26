package com.test.task.videostream.service.message;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaMessageService implements MessageService {
    private final KafkaTemplate<String, Object> template;

    @Override
    public <T> void sendMessage(String topic, T message) {
        template.send(topic, message);
    }
}
