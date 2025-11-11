package com.football.team.service;

import com.football.team.dto.PlayerDTO;
import com.football.team.dto.PresetDTO;
import com.football.team.dto.TeamDTO;
import com.football.team.mapper.PlayerMapper;
import com.football.team.mapper.PresetMapper;
import com.football.team.model.*;
import com.football.team.repository.PlacementRepository;
import com.football.team.repository.TeamRepository;
import com.football.team.repository.UserTeamRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private final UserTeamRepository userTeamRepository;
    private final TeamRepository teamRepository;
    private final PlacementRepository placementRepository;
    private final PlayerMapper playerMapper;
    private final PresetMapper presetMapper;

    public TeamService(TeamRepository teamRepository, PlacementRepository placementRepository, PlayerMapper playerMapper, PresetMapper presetMapper, UserTeamRepository userTeamRepository) {
        this.teamRepository = teamRepository;
        this.placementRepository = placementRepository;
        this.playerMapper = playerMapper;
        this.presetMapper = presetMapper;
        this.userTeamRepository = userTeamRepository;
    }


    public TeamDTO findTeamByName(String name) {
        Team teamsByName = teamRepository.findTeamsByName(name);
        List<PresetDTO> presetDTOS = buildPresetDtoByTeamId(teamsByName.getId());

        return TeamDTO.builder()
                .Id(teamsByName.getId())
                .name(teamsByName.getName())
                .presets(presetDTOS)
                .build();

    }

    public Team createTeamByName(String name) {
        Team team = Team.builder().name(name).build();
        Preset preset = Preset.builder().presetId(1).presetFormation("4-3-3").presetStrategy("balacend").build();
        preset.setTeam(team);

        List<Placement> placements = new ArrayList<>();
        for (String[] playerData : generateListPlayers()) {
            Player player = new Player();
            player.setName(playerData[0]);
            player.setNumber(playerData[1]);
            player.setPosition(playerData[2]);

            Placement placement = new Placement();
            placement.setPlayer(player);
            placement.setPreset(preset);
            placement.setTop(playerData[3]);
            placement.setLeft(playerData[4]);
            placements.add(placement);
        }

        preset.setPlayerPlacements(placements);
        team.setPresets(List.of(preset));

        return teamRepository.save(team);
    }


    private List<String[]>  generateListPlayers() {
        List<String[]> playerDataDefault = Arrays.asList(
                new String[]{"Goleiro", "1", "GK", "94%", "50%"},
                new String[]{"Lateral Direito", "2", "RB", "75%", "85%"},
                new String[]{"Zagueiro Direito", "3", "CB", "80%", "65%"},
                new String[]{"Zagueiro Esquerdo", "4", "CB", "80%", "35%"},
                new String[]{"Lateral Esquerdo", "6", "LB", "75%", "15%"},
                new String[]{"Volante", "5", "CDM", "55%", "50%"},
                new String[]{"Meia Direito", "8", "CM", "45%", "65%"},
                new String[]{"Meia Esquerdo", "10", "CM", "45%", "35%"},
                new String[]{"Ponta Direita", "7", "RW", "20%", "75%"},
                new String[]{"Centroavante", "9", "ST", "10%", "50%"},
                new String[]{"Ponta Esquerda", "11", "LW", "20%", "25%"}
        );
        return playerDataDefault;
    }

    public List<PresetDTO> buildPresetDtoByTeamId(Integer teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team ID not found: " + teamId));

        return team.getPresets().stream()
                .map(preset -> {
                    List<PlayerDTO> playerDTOs = preset.getPlayerPlacements().stream()
                            .map(placement -> PlayerMapper.INSTANCE.playerToPlayerDTO(
                                    placement.getPlayer(), placement))
                            .collect(Collectors.toList());

                    return PresetDTO.builder()
                            .presetId(preset.getPresetId())
                            .presetFormation(preset.getPresetFormation())
                            .presetStrategy(preset.getPresetStrategy())
                            .presetPlayers(playerDTOs)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public Preset createPreset(PresetDTO presetDTO, Integer id) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new RuntimeException("Team ID not found: " + id));
        Preset preset = presetMapper.presetDTOToPreset(presetDTO);
        preset.setTeam(team);

        if (preset.getPlayerPlacements() != null) {
            preset.getPlayerPlacements().forEach(p -> p.setPreset(preset));
        }

        if (team.getPresets() == null) {
            team.setPresets(new ArrayList<>());
        }
        team.getPresets().add(preset);
        teamRepository.save(team);
        return preset;
    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = "user-team", partitions = { "0" }), containerFactory = "kafkaListenerContainerFactory")
    public void createUserTeam(UserRequest userRequest) {
        Team teamByName = createTeamByName(userRequest.getTeamName());
        UserTeam userTeam = new UserTeam();
        userTeam.setTeamName(userRequest.getTeamName());
        userTeam.setAccount(userRequest.getAccount());
        userTeam.setTeam(teamByName);
        userTeamRepository.save(userTeam);
        teamRepository.save(teamByName);

    }


}
