package pl.jenczalik.casinogame.adapters.api;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.jenczalik.casinogame.domain.model.GameState;
import pl.jenczalik.casinogame.domain.model.GameType;

@Getter
@AllArgsConstructor
public class GameStateResponse {
    private UUID gameId;
    private BigDecimal balance;
    private int freeRounds;
    private String playerId;
    private GameType gameType;

    public static GameStateResponse fromDomain(GameState gameState) {
        return new GameStateResponse(
                gameState.getGameId(),
                gameState.getBalance(),
                gameState.getFreeRounds(),
                gameState.getPlayer().getId().toString(),
                gameState.getGameType()
        );
    }
}
