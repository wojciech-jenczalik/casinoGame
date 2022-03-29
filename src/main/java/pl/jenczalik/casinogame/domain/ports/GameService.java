package pl.jenczalik.casinogame.domain.ports;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;
import pl.jenczalik.casinogame.config.CashPolicyConfig;
import pl.jenczalik.casinogame.domain.model.CashDeductionPolicy;
import pl.jenczalik.casinogame.domain.model.GameNotFoundException;
import pl.jenczalik.casinogame.domain.model.GameStartDetails;
import pl.jenczalik.casinogame.domain.model.GameState;
import pl.jenczalik.casinogame.domain.model.GameType;
import pl.jenczalik.casinogame.domain.model.PlayRoundDetails;
import pl.jenczalik.casinogame.domain.model.RoundResult;

@Log4j2
public class GameService {
    private final CashPolicyConfig cashPolicyConfig;
    private final Map<GameType, CashDeductionPolicy> cashDeductionPoliciesMap;
    private final GameStateRepository gameStateRepository;
    private final RoundService roundService;

    public GameService(CashPolicyConfig cashPolicyConfig,
                       List<CashDeductionPolicy> cashDeductionPolicies,
                       GameStateRepository gameStateRepository,
                       RoundService roundService) {
        this.cashPolicyConfig = cashPolicyConfig;
        this.cashDeductionPoliciesMap = cashDeductionPolicies.stream()
                .collect(Collectors.toMap(
                        CashDeductionPolicy::getGameType,
                        Function.identity()
                ));
        this.gameStateRepository = gameStateRepository;
        this.roundService = roundService;
    }

    public GameState startGame(GameStartDetails gameStartDetails) {
        validateGameStartDetails(gameStartDetails);

        final GameState newGame = GameState.newGame(
                gameStartDetails.getGameType(),
                gameStartDetails.getPlayer(),
                cashPolicyConfig.getDefaultInitialBalance());
        log.info("started new game with ID {}, balance {}, player ID {}", newGame.getGameId(), newGame.getBalance(), newGame.getPlayer().getId());

        return gameStateRepository.save(newGame);
    }

    public GameState playRound(PlayRoundDetails playRoundDetails) {
        log.debug("game {}. Playing new round with a bet {}, by player {}",
                playRoundDetails.getGameId(),
                playRoundDetails.getBet(),
                playRoundDetails.getPlayerId());

        final UUID gameId = playRoundDetails.getGameId();
        final UUID playerId = playRoundDetails.getPlayerId();
        validatePlayRoundDetails(gameId, playerId);

        final BigDecimal bet = playRoundDetails.getBet();
        GameState gameState = gameStateRepository.getByGameIdAndPlayerId(gameId, playerId)
                .orElseThrow(() -> new GameNotFoundException(String.format("game with ID [%s] not found for user with ID [%s]", gameId, playerId)));
        validateBet(bet, gameState.getBalance());

        final CashDeductionPolicy deductionPolicy;
        if (gameState.getFreeRounds() > 0) {
            log.debug("game {}. Spending a free round", gameId);
            gameState.decrementFreeRounds();
            deductionPolicy = cashDeductionPoliciesMap.get(GameType.FREE);
        } else {
            log.trace("game {}. No free round to spend", gameId);
            deductionPolicy = cashDeductionPoliciesMap.get(gameState.getGameType());
        }

        gameState.deductBalance(deductionPolicy, bet);

        final RoundResult roundResult = roundService.playRound(bet, gameId);
        if (roundResult.isFreeRoundWon()) {
            log.debug("game {}. Free round won", gameId);
            gameState.incrementFreeRounds();
        }
        gameState.addToBalance(roundResult.getWinnings());
        return gameStateRepository.save(gameState);
    }

    private void validateGameStartDetails(GameStartDetails gameStartDetails) {
        if (gameStartDetails.getPlayer() == null) {
            throw new IllegalStateException("could not start new game. Player is null");
        }
        final CashDeductionPolicy deductionPolicy = cashDeductionPoliciesMap.get(gameStartDetails.getGameType());
        if (deductionPolicy == null) {
            throw new IllegalStateException(String.format(
                    "could not start a new game. Cash deduction policy not found for game type: %s",
                    gameStartDetails.getGameType()));
        }
    }

    private void validatePlayRoundDetails(UUID gameId, UUID playerId) {
        if (gameId == null) {
            throw new IllegalArgumentException("gameId must not be null");
        }
        if (playerId == null) {
            throw new IllegalArgumentException("playerId must not be null");
        }
    }

    private void validateBet(BigDecimal bet, BigDecimal balance) {
        final BigDecimal minBetValue = cashPolicyConfig.getMinBetValue();
        final BigDecimal maxBetValue = cashPolicyConfig.getMaxBetValue();

        if (bet.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(String.format("bet (%s) can not be a negative amount", bet));
        }
        if (bet.compareTo(minBetValue) < 0) {
            throw new IllegalArgumentException(String.format("bet (%s) can not be less than %s", bet, minBetValue));
        }
        if (bet.compareTo(maxBetValue) > 0) {
            throw new IllegalArgumentException(String.format("bet (%s) can not be more than %s", bet, maxBetValue));
        }
        if (bet.compareTo(balance) > 0) {
            throw new IllegalArgumentException(String.format("bet (%s) can not be greater than current balance (%s)", bet, balance));
        }
    }
}
