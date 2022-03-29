package pl.jenczalik.casinogame.domain.services;

import java.math.BigDecimal;

import pl.jenczalik.casinogame.domain.model.GameType;

public interface CashDeductionPolicy {
    GameType getGameType();
    BigDecimal deductBetFromBalance(BigDecimal bet, BigDecimal balance);
}
