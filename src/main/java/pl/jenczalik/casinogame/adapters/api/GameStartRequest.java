package pl.jenczalik.casinogame.adapters.api;

import java.util.UUID;

import lombok.Data;
import pl.jenczalik.casinogame.domain.model.GameType;

@Data
public class GameStartRequest {
    private GameType gameType;
    private UUID playerId;
}
