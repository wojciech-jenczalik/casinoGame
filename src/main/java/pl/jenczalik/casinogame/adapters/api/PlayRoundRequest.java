package pl.jenczalik.casinogame.adapters.api;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;

@Data
public class PlayRoundRequest {
    private UUID playerId;
    private BigDecimal bet;
}
