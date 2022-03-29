package pl.jenczalik.casinogame.domain.model;

import java.math.BigDecimal;

import lombok.Value;

@Value
public class RoundResult {
    BigDecimal winnings;
    boolean freeRoundWon;
}
