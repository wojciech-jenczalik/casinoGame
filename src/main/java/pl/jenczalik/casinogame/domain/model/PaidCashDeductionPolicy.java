package pl.jenczalik.casinogame.domain.model;

import java.math.BigDecimal;

import lombok.extern.log4j.Log4j2;

@Log4j2
class PaidCashDeductionPolicy implements CashDeductionPolicy {
    @Override
    public GameType getGameType() {
        return GameType.PAID;
    }

    @Override
    public BigDecimal deductBetFromBalance(BigDecimal bet, BigDecimal balance) {
        log.debug("deducting bet ({}) from balance ({})", bet, balance);
        if (bet.compareTo(balance) > 0) {
            throw new IllegalStateException(String.format("placed bet (%s) is greater than balance (%s)", bet, balance));
        }
        return balance.subtract(bet);
    }

    public static CashDeductionPolicy create() {
        return new PaidCashDeductionPolicy();
    }
}
