package com.football.match.controller;

import com.football.match.model.Match;
import com.football.match.model.MatchSummary;
import com.football.match.service.MatchService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/matchs")
public class MatchController {

    private final MatchService matchService;
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping
    public ResponseEntity<Match> createMatch(@RequestBody Match match) {
        return ResponseEntity.ok(matchService.createMatch(match));
    }

    @GetMapping("/page")
    public Page<MatchSummary> listMatches(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        return matchService.listPageMatch(page, size);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable String id) {
        matchService.deleteMatch(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
