package pl.jenczalik.casinogame.domain.model;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GameState {
    private final CashDeductionPolicy cashDeductionPolicy;
    private final Player player;
    private final BigDecimal balance;

    public static GameState newGame(CashDeductionPolicy cashDeductionPolicy, Player player, BigDecimal balance) {
        return new GameState(cashDeductionPolicy, player, balance);
    }
}
