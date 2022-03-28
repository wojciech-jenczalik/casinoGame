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
    private static final BigDecimal DEFAULT_STARTING_BALANCE = BigDecimal.valueOf(5000d);

    private CashDeductionPolicy cashDeductionPolicy;
    private Player player;
    private BigDecimal balance;

    public static GameState newPaidGameWithPlayer(Player player) {
        return new GameState(PaidCashDeductionPolicy.create(), player, DEFAULT_STARTING_BALANCE);
    }

    public static GameState newFreeGameWithPlayer(Player player) {
        return new GameState(FreeCashDeductionPolicy.create(), player, DEFAULT_STARTING_BALANCE);
    }
}
