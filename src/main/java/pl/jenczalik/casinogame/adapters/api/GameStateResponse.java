package pl.jenczalik.casinogame.adapters.api;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.jenczalik.casinogame.domain.model.GameState;

@Getter
@AllArgsConstructor
public class GameStateResponse {
    private UUID gameId;
    private BigDecimal balance;
    private String playerId;

    public static GameStateResponse fromDomain(GameState gameState) {
        return new GameStateResponse(
                gameState.getGameId(),
                gameState.getBalance(),
                gameState.getPlayer().getId().toString()
        );
    }
}
