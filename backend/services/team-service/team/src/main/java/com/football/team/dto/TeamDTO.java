package com.football.team.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamDTO {
    private Integer Id;
    private String name;
    private List<PresetDTO> presets;

}
