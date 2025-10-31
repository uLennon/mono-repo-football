package com.football.team.controller;

import com.football.team.dto.PresetDTO;
import com.football.team.dto.TeamDTO;
import com.football.team.model.Preset;
import com.football.team.model.Team;
import com.football.team.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("teams")
public class TeamController {

    private final TeamService teamService;
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/find")
    public ResponseEntity<TeamDTO> getTeamByName(@RequestParam String name){
        return ResponseEntity.ok(teamService.findTeamByName(name));
    }

    @PostMapping()
    public ResponseEntity<Team> createTeam(@RequestParam String name){
        return ResponseEntity.ok(teamService.createTeamByName(name));
    }

    @GetMapping("/find-preset")
    public ResponseEntity<List<PresetDTO>> getListPresets(@RequestParam Integer id){
        return ResponseEntity.ok(teamService.buildPresetDtoByTeamId(id));
    }

    @PostMapping("/preset")
    public ResponseEntity<Preset> createPreset(@RequestBody PresetDTO presetDTO, @RequestParam Integer id){
        return ResponseEntity.ok(teamService.createPreset(presetDTO,id));
    }
}
