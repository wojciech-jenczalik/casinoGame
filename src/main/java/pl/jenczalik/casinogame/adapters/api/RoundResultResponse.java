package pl.jenczalik.casinogame.adapters.api;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.jenczalik.casinogame.domain.model.RoundResult;

@Getter
@AllArgsConstructor
class RoundResultResponse {
    private final BigDecimal winnings;
    private final boolean freeRoundWon;
    private final LocalDateTime playDateTime;

    static RoundResultResponse fromDomain(RoundResult roundResult) {
        return new RoundResultResponse(
                roundResult.getWinnings(),
                roundResult.isFreeRoundWon(),
                roundResult.getPlayDateTime()
        );
    }
}
