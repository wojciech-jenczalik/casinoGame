package pl.jenczalik.casinogame.domain.services;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.jenczalik.casinogame.config.RoundRewardsConfig;
import pl.jenczalik.casinogame.domain.model.RoundResult;
import pl.jenczalik.casinogame.domain.ports.RoundResultRepository;

@Log4j2
@RequiredArgsConstructor
public class RoundService {
    private final RoundRewardsConfig roundRewardsConfig;
    private final SecureRandom random;
    private final RoundResultRepository roundResultRepository;
    private final Clock clock;

    @Transactional(propagation = Propagation.MANDATORY)
    public RoundResult playRound(BigDecimal bet, UUID gameId) {
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

        log.debug("game {}. Round results: {} winnings and free round won ({})", gameId, winnings, isFreeRoundWon);

        return roundResultRepository.save(new RoundResult(winnings, isFreeRoundWon, gameId, LocalDateTime.now(clock)));
    }

    public List<RoundResult> getRoundsForGame(UUID gameId) {
        return roundResultRepository.getRoundsByGameId(gameId);
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
