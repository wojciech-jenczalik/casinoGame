package pl.jenczalik.casinogame.domain.model;

import java.math.BigDecimal;

public interface CashDeductionPolicy {
    GameType getGameType();
    BigDecimal deductBetFromBalance(BigDecimal bet, BigDecimal balance);
}
