package com.football.team.mapper;


import com.football.team.dto.PlayerDTO;
import com.football.team.dto.PresetDTO;
import com.football.team.model.Placement;
import com.football.team.model.Preset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = PlayerMapper.class)
public interface PresetMapper {

    @Mapping(target = "presetPlayers", expression = "java(mapPlayers(preset))")
    PresetDTO presetToPresetDTO(Preset preset);

    @Mapping(target = "playerPlacements", expression = "java(mapPlayerPlacements(presetDTO.getPresetPlayers()))")
    Preset presetDTOToPreset(PresetDTO presetDTO);

    default List<Placement> mapPlayerPlacements(List<PlayerDTO> playerDTOs) {
        if (playerDTOs == null) return List.of();
        return playerDTOs.stream()
                .map(PlayerMapper.INSTANCE::playerDTOToPlacement)
                .collect(Collectors.toList());
    }


    default List<PlayerDTO> mapPlayers(Preset preset) {
        if (preset.getPlayerPlacements() == null) return List.of();

        return preset.getPlayerPlacements().stream()
                .map(placement -> PlayerMapper.INSTANCE.playerToPlayerDTO(
                        placement.getPlayer(), placement))
                .toList();
    }

}