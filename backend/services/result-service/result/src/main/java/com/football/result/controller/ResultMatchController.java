package com.football.result.controller;

import com.football.result.model.ResultMatch;
import com.football.result.service.ResultMatchService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/results")
@CrossOrigin(origins = "http://localhost:4200")
public class ResultMatchController {


    private final ResultMatchService resultMatchService;
    public ResultMatchController(ResultMatchService resultMatchService) {
        this.resultMatchService = resultMatchService;
    }

    @GetMapping(value = "/conectar", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter conectar() {
        return resultMatchService.createEmitter();
    }

    @GetMapping("/page")
    public Page<ResultMatch> listPageResulMatchs(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "6") int size,
                                                 @RequestParam String nameTeam) {
        return resultMatchService.resultMatchPage(page, size,nameTeam);
    }
}
