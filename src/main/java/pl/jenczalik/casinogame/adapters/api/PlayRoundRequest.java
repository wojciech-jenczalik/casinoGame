package pl.jenczalik.casinogame.adapters.api;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;
import pl.jenczalik.casinogame.domain.model.GameMode;

@Data
public class PlayRoundRequest {
    private UUID playerId;
    private BigDecimal bet;
    private GameMode gameMode;
}
