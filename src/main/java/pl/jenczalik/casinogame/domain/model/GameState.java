package pl.jenczalik.casinogame.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import pl.jenczalik.casinogame.domain.services.CashDeductionPolicy;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Log4j2
public class GameState {
    private final UUID gameId;
    private final Player player;
    private BigDecimal balance;

    public static GameState newGame(Player player, BigDecimal balance) {
        return new GameState(UUID.randomUUID(), player, balance);
    }

    public void addToBalance(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("can not add negative amount to balance");
        }
        balance = balance.add(amount);
    }

    public void deductBetFromBalance(CashDeductionPolicy deductionPolicy, BigDecimal bet) {
        if (bet.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("can not place negative bet");
        }
        if (deductionPolicy == null) {
            throw new IllegalStateException("deduction policy is null");
        }
        log.debug("game {}. Deducting bet [{}] from balance using {} policy", gameId, bet, deductionPolicy.getGameType());
        balance = deductionPolicy.deductBetFromBalance(bet, balance);
    }
}
