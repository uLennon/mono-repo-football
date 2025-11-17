package com.football.team.mapper;

import com.football.team.dto.PlayerDTO;
import com.football.team.model.Placement;
import com.football.team.model.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Map;


@Mapper(componentModel = "spring")
public interface PlayerMapper {

    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);
    @Mapping(source = "player.id", target = "id")
    @Mapping(source = "player.name", target = "name")
    @Mapping(source = "player.position", target = "position")
    @Mapping(source = "player.number", target = "number")
    @Mapping(target = "coords", expression = "java(mapCoords(placement))")
    PlayerDTO playerToPlayerDTO(Player player, Placement placement);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "position", source = "position")
    @Mapping(target = "number", source = "number")
    Player playerDTOToPlayer(PlayerDTO playerDTO);

    default Placement playerDTOToPlacement(PlayerDTO playerDTO) {
        if (playerDTO == null) {
            return null;
        }

        Placement placement = new Placement();
        placement.setPlayer(playerDTOToPlayer(playerDTO));

        if (playerDTO.getCoords() != null) {
            placement.setTop(playerDTO.getCoords().get("top"));
            placement.setLeft(playerDTO.getCoords().get("left"));
        }

        return placement;
    }

    default Map<String, String> mapCoords(Placement placement) {
        if (placement == null) return null;
        return Map.of("top", placement.getTop(), "left", placement.getLeft());
    }
}
