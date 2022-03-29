package pl.jenczalik.casinogame.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Value;

@Value
public class RoundResult {
    BigDecimal winnings;
    boolean freeRoundWon;
    UUID gameId;
    LocalDateTime playDateTime;
}
