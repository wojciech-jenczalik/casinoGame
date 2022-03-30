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
    private UUID playerId;
    private BigDecimal balance;

    GameState toDomain() {
        return GameState.builder()
                .gameId(gameId)
                .player(Player.fromId(playerId))
                .balance(balance)
                .build();
    }

    static GameStateEntity fromDomain(GameState gameState) {
        return GameStateEntity.builder()
                .gameId(gameState.getGameId())
                .playerId(gameState.getPlayer().getId())
                .balance(gameState.getBalance())
                .build();
    }
}
