package pl.jenczalik.casinogame.domain.ports;

import java.math.BigDecimal;
import java.util.Random;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.jenczalik.casinogame.config.RoundRewardsConfig;
import pl.jenczalik.casinogame.domain.model.RoundResult;

@Log4j2
@RequiredArgsConstructor
class RoundService {
    private final RoundRewardsConfig roundRewardsConfig;

    // TODO test
    RoundResult playRound(BigDecimal bet) {
        final BigDecimal cashWinRand = BigDecimal.valueOf(new Random().nextInt(100));
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

        final BigDecimal freeRoundWinRand = BigDecimal.valueOf(new Random().nextInt(100));
        final boolean isFreeRoundWon = freeRoundWinRand.compareTo(roundRewardsConfig.getFreeRoundWinChancePercentage()) < 0;

        // TODO persist round result

        return new RoundResult(winnings, isFreeRoundWon);
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
