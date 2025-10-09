package com.football.team.controller;

import com.football.team.model.Team;
import com.football.team.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/find")
    public ResponseEntity<Team> findTeamByName(@RequestParam String name) {
        return ResponseEntity.ok(teamService.findByName(name));
    }

}
