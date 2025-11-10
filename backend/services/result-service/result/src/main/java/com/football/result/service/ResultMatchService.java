package com.football.result.service;

import com.football.result.model.PlayMatch;
import com.football.result.model.ResultMatch;
import com.football.result.model.Team;
import com.football.result.repository.ResultMachRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ResultMatchService {

    private final IaGenerativeService iaGenerativeService;
    private final ResultMachRepository resultMachRepository;

    public ResultMatchService(IaGenerativeService iaGenerativeService, ResultMachRepository resultMachRepository) {
        this.iaGenerativeService = iaGenerativeService;
        this.resultMachRepository = resultMachRepository;
    }


    @KafkaListener(topicPartitions = @TopicPartition(topic = "match-topic", partitions = { "0" }), containerFactory = "kafkaListenerContainerFactory")
    public void creatResultMatch(PlayMatch playMatch) {
        Team first = playMatch.getTeams().getFirst();
        Team last = playMatch.getTeams().getLast();
        ResultMatch resultMatch = iaGenerativeService.buildResultMatch(first, last);
        resultMatch.setMatchDate(playMatch.getMatchDate());
        resultMatch.setTeams(playMatch.getTeams());
        resultMachRepository.save(resultMatch);
        System.out.println(resultMatch);
        enviarParaTodosClientes(resultMatch);

    }


    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();


    public SseEmitter createEmitter() {
        SseEmitter emitter = new SseEmitter(0L);

        emitters.add(emitter);

        emitter.onCompletion(() -> {
            System.out.println("Front disconnect");
            emitters.remove(emitter);
        });

        emitter.onTimeout(() -> {
            System.out.println(" Timeout do front");
            emitters.remove(emitter);
        });

        emitter.onError((throwable) -> {
            System.out.println(" Error emitter: " + throwable.getMessage());
        });

        System.out.println("connecters. Total: " + emitters.size());
        return emitter;
    }

    public void enviarParaTodosClientes(ResultMatch resultMatch) {
        System.out.println("Send para " + emitters.size() + " Front...");

        List<SseEmitter> emittersMortos = new ArrayList<>();

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("partida-nova")
                        .data(resultMatch, MediaType.APPLICATION_JSON));

                System.out.println("✅ Send to front: " + resultMatch.getDescription());

            } catch (Exception e) {
                System.out.println(" Error send to front: " + e.getMessage());
                emittersMortos.add(emitter);

                try {
                    emitter.completeWithError(e);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        emitters.removeAll(emittersMortos);
        System.out.println("fronts: " + emitters.size());
    }

    public Page<ResultMatch> resultMatchPage(int page, int size) {
        return resultMachRepository.findAll(PageRequest.of(page,size));
    }

}
