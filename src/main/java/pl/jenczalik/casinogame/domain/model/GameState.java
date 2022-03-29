package pl.jenczalik.casinogame.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)

// TODO add loggers
public class GameState {
    private final UUID gameId;
    private final GameType gameType;
    private final Player player;
    private BigDecimal balance;
    private int freeRounds;

    public static GameState newGame(GameType gameType, Player player, BigDecimal balance) {
        return new GameState(UUID.randomUUID(), gameType, player, balance, 0);
    }

    public void addToBalance(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public void deductBalance(CashDeductionPolicy deductionPolicy, BigDecimal deductionAmount) {
        balance = deductionPolicy.deductBetFromBalance(deductionAmount, balance);
    }

    public void incrementFreeRounds() {
        ++freeRounds;
    }

    public void decrementFreeRounds() {
        --freeRounds;
    }
}
