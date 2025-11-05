package com.football.result.service;

import com.football.result.model.PlayMatch;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

@Service
public class ResultMatchService {

    @KafkaListener(topicPartitions = @TopicPartition(topic = "match-topic", partitions = { "0" }), containerFactory = "kafkaListenerContainerFactory")
    public void creatResultMatch(PlayMatch playMatch) {

    }

}
