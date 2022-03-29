package pl.jenczalik.casinogame.domain.ports;

import java.util.UUID;

import pl.jenczalik.casinogame.domain.model.GameState;

public interface GameStateRepository {
    GameState save(GameState gameState);
    GameState getByGameIdAndPlayerId(UUID gameId, UUID playerId);
}
