package pl.jenczalik.casinogame.domain.services;

import java.math.BigDecimal;

import lombok.extern.log4j.Log4j2;
import pl.jenczalik.casinogame.domain.model.GameMode;

@Log4j2
public class PaidCashDeductionPolicy implements CashDeductionPolicy {
    @Override
    public GameMode getGameMode() {
        return GameMode.PAID;
    }

    @Override
    public BigDecimal deductBetFromBalance(BigDecimal bet, BigDecimal balance) {
        log.debug("deducting bet ({}) from balance ({})", bet, balance);
        return balance.subtract(bet);
    }

    public static CashDeductionPolicy create() {
        return new PaidCashDeductionPolicy();
    }
}
