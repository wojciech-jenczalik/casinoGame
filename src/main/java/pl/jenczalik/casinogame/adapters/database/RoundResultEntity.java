package pl.jenczalik.casinogame.adapters.database;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.jenczalik.casinogame.domain.model.RoundResult;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
class RoundResultEntity {
    @Id
    private UUID roundResultId;
    private BigDecimal winnings;
    private boolean freeRoundWon;
    private UUID gameId;
    private LocalDateTime playDateTime;

    RoundResult toDomain() {
        return new RoundResult(winnings, freeRoundWon, gameId, playDateTime);
    }

    static RoundResultEntity fromDomain(RoundResult roundResult) {
        return RoundResultEntity.builder()
                .roundResultId(UUID.randomUUID())
                .winnings(roundResult.getWinnings())
                .freeRoundWon(roundResult.isFreeRoundWon())
                .gameId(roundResult.getGameId())
                .playDateTime(roundResult.getPlayDateTime())
                .build();
    }
}
