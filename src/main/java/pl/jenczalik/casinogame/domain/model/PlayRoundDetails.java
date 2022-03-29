package pl.jenczalik.casinogame.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class PlayRoundDetails {
    private final UUID gameId;
    private final UUID playerId;
    private final BigDecimal bet;
}
