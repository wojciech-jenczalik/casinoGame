package pl.jenczalik.casinogame.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.With;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GameState {
    private final UUID gameId;
    private final GameType gameType;
    private final Player player;
    @With
    private final BigDecimal balance;
    @With
    private final int freeRounds;

    public static GameState newGame(GameType gameType, Player player, BigDecimal balance) {
        return new GameState(UUID.randomUUID(), gameType, player, balance, 0);
    }

    // TODO test
    public GameState addToBalance(BigDecimal amount) {
        final BigDecimal newBalance = balance.add(amount);
        return this.withBalance(newBalance);
    }

    // TODO test
    public GameState deductBalance(CashDeductionPolicy deductionPolicy, BigDecimal deductionAmount) {
        final BigDecimal newBalance = deductionPolicy.deductBetFromBalance(deductionAmount, balance);
        return this.withBalance(newBalance);
    }

    // TODO test
    public GameState incrementFreeRounds() {
        final int newFreeRounds = freeRounds + 1;
        return this.withFreeRounds(newFreeRounds);
    }

    public GameState decrementFreeRounds() {
        final int newFreeRounds = freeRounds - 1;
        return this.withFreeRounds(newFreeRounds);
    }
}
