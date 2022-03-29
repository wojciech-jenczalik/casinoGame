package pl.jenczalik.casinogame.domain.ports;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.jenczalik.casinogame.config.RoundRewardsConfig;
import pl.jenczalik.casinogame.domain.model.RoundResult;

@Log4j2
@RequiredArgsConstructor
public class RoundService {
    private final RoundRewardsConfig roundRewardsConfig;
    private final SecureRandom random;
    private final RoundResultRepository roundResultRepository;
    private final Clock clock;

    RoundResult playRound(BigDecimal bet, UUID gameId) {
        final BigDecimal cashWinRand = BigDecimal.valueOf(random.nextInt(100));
        final BigDecimal winnings;

        if (smallWin(cashWinRand)) {
            winnings = bet.multiply(roundRewardsConfig.getSmallWinMultiplier());
        } else if (mediumWin(cashWinRand)) {
            winnings = bet.multiply(roundRewardsConfig.getMediumWinMultiplier());
        } else if (bigWin(cashWinRand)) {
            winnings = bet.multiply(roundRewardsConfig.getBigWinMultiplier());
        } else {
            winnings = BigDecimal.ZERO;
        }

        final BigDecimal freeRoundWinRand = BigDecimal.valueOf(random.nextInt(100));
        final boolean isFreeRoundWon = freeRoundWinRand.compareTo(roundRewardsConfig.getFreeRoundWinChancePercentage()) < 0;

        return roundResultRepository.save(new RoundResult(winnings, isFreeRoundWon, gameId, LocalDateTime.now(clock)));
    }

    private boolean smallWin(BigDecimal rand) {
        return rand.compareTo(roundRewardsConfig.getSmallWinChancePercentage()) < 0;
    }

    private boolean mediumWin(BigDecimal rand) {
        return rand.compareTo(
                roundRewardsConfig.getSmallWinChancePercentage()
                        .add(roundRewardsConfig.getMediumWinChancePercentage())) < 0;
    }

    private boolean bigWin(BigDecimal rand) {
        return rand.compareTo(
                roundRewardsConfig.getSmallWinChancePercentage()
                        .add(roundRewardsConfig.getMediumWinChancePercentage())
                        .add(roundRewardsConfig.getBigWinChancePercentage())) < 0;
    }
}
