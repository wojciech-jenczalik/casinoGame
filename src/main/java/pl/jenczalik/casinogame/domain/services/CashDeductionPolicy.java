package pl.jenczalik.casinogame.domain.services;

import java.math.BigDecimal;

import pl.jenczalik.casinogame.domain.model.GameMode;

public interface CashDeductionPolicy {
    GameMode getGameMode();
    BigDecimal deductBetFromBalance(BigDecimal bet, BigDecimal balance);
}
