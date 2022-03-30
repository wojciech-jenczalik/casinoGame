package pl.jenczalik.casinogame.domain.ports;

import java.util.List;
import java.util.UUID;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.jenczalik.casinogame.domain.model.GameState;

public interface GameStateRepository {
    @Transactional(propagation = Propagation.MANDATORY)
    GameState save(GameState gameState);

    GameState getByGameIdAndPlayerId(UUID gameId, UUID playerId);
    GameState getByGameId(UUID gameId);
    List<GameState> getAllByPlayerId(UUID playerId);
}
