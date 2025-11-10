package com.football.auth.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${kafka.myServe}")
    private String myServe;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        HashMap<String, Object> config = new HashMap<>();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, myServe);
        return new KafkaAdmin(config);
    }

    @Bean
    public NewTopic myTopic() {
        return new NewTopic("user-team", 1, (short) 1);
    }

}
