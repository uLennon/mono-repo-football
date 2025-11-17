package com.football.team.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PresetDTO {
    private Integer presetId;
    private String presetFormation;
    private String presetStrategy;
    private List<PlayerDTO> presetPlayers;
}
