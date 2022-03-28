package pl.jenczalik.casinogame.domain.model;

import java.math.BigDecimal;

interface CashDeductionPolicy {
    GameType getGameType();
    BigDecimal deductBetFromBalance(BigDecimal bet, BigDecimal balance);
}
