package pl.jenczalik.casinogame.adapters.database;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.jenczalik.casinogame.domain.model.GameState;
import pl.jenczalik.casinogame.domain.model.GameType;
import pl.jenczalik.casinogame.domain.model.Player;

@Entity
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
class GameStateEntity {
    @Id
    private UUID gameId;
    private GameType gameType;
    private UUID playerId;
    private BigDecimal balance;
    private int freeRounds;

    GameState toDomain() {
        return GameState.builder()
                .gameId(gameId)
                .gameType(gameType)
                .player(Player.fromId(playerId))
                .balance(balance)
                .freeRounds(freeRounds)
                .build();
    }

    static GameStateEntity fromDomain(GameState gameState) {
        return GameStateEntity.builder()
                .gameId(gameState.getGameId())
                .gameType(gameState.getGameType())
                .playerId(gameState.getPlayer().getId())
                .balance(gameState.getBalance())
                .freeRounds(gameState.getFreeRounds())
                .build();
    }
}
