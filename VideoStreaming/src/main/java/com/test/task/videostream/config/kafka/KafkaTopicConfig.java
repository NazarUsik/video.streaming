package com.test.task.videostream.config.kafka;

import lombok.Getter;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Getter
@Configuration
public class KafkaTopicConfig {
    @Value("${spring.kafka.topic}")
    private String topic;

    @Bean
    public NewTopic topic() {
        return new NewTopic(topic, 1, (short) 1);
    }
}
