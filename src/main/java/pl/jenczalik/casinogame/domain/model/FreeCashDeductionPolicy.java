package pl.jenczalik.casinogame.domain.model;

import java.math.BigDecimal;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class FreeCashDeductionPolicy implements CashDeductionPolicy {
    @Override
    public GameType getGameType() {
        return GameType.FREE;
    }

    @Override
    public BigDecimal deductBetFromBalance(BigDecimal bet, BigDecimal balance) {
        validateBet(bet);
        log.debug("not deducting bet ({}) from balance ({}). Player is using free game mode", bet, balance);
        return balance;
    }

    public static CashDeductionPolicy create() {
        return new FreeCashDeductionPolicy();
    }

    private void validateBet(BigDecimal bet) {
        if (bet.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException(String.format("bet (%s) can not be a negative amount", bet));
        }
        log.debug("bet valid");
    }
}
