package pl.jenczalik.casinogame.domain.services;

import java.math.BigDecimal;

import lombok.extern.log4j.Log4j2;
import pl.jenczalik.casinogame.domain.model.GameType;

@Log4j2
public class FreeCashDeductionPolicy implements CashDeductionPolicy {
    @Override
    public GameType getGameType() {
        return GameType.FREE;
    }

    @Override
    public BigDecimal deductBetFromBalance(BigDecimal bet, BigDecimal balance) {
        log.debug("not deducting bet ({}) from balance ({}). Player is using free game mode", bet, balance);
        return balance;
    }

    public static CashDeductionPolicy create() {
        return new FreeCashDeductionPolicy();
    }
}
