package pl.jenczalik.casinogame.domain.ports;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;
import pl.jenczalik.casinogame.config.CashPolicyConfig;
import pl.jenczalik.casinogame.domain.model.GameHistory;
import pl.jenczalik.casinogame.domain.model.GameState;
import pl.jenczalik.casinogame.domain.model.GameType;
import pl.jenczalik.casinogame.domain.model.Player;
import pl.jenczalik.casinogame.domain.model.RoundResult;
import pl.jenczalik.casinogame.domain.services.CashDeductionPolicy;
import pl.jenczalik.casinogame.domain.services.RoundService;

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

    @Transactional
    public GameState startGame(Player player) {
        validateGameStart(player);

        final GameState newGame = GameState.newGame(player, cashPolicyConfig.getDefaultInitialBalance());
        log.info("started new game with ID {}, balance {}, player ID {}", newGame.getGameId(), newGame.getBalance(), newGame.getPlayer().getId());

        return gameStateRepository.save(newGame);
    }

    @Transactional
    public GameState playRound(GameType gameType, UUID gameId, UUID playerId, BigDecimal bet) {
        boolean isFreeRoundWon = false;
        GameState gameStateAfterRound;
        do {
            log.debug("game {}. Playing new round with a bet {}, by player {} in game mode: {}", gameId, bet, playerId, gameType);
            validatePlayRound(gameType, gameId, playerId);

            final GameState gameState = gameStateRepository.getByGameIdAndPlayerId(gameId, playerId);
            validateBet(bet, gameState.getBalance());

            final CashDeductionPolicy cashDeductionPolicy = isFreeRoundWon ?
                    cashDeductionPoliciesMap.get(GameType.FREE) :
                    cashDeductionPoliciesMap.get(gameType);

            gameState.deductBetFromBalance(cashDeductionPolicy, bet);

            final RoundResult roundResult = roundService.playRound(bet, gameId);
            gameState.addToBalance(roundResult.getWinnings());

            gameStateAfterRound = gameStateRepository.save(gameState);

            isFreeRoundWon = roundResult.isFreeRoundWon();
        } while (isFreeRoundWon);

        return gameStateAfterRound;
    }

    public GameHistory getGameHistory(UUID gameId) {
        final GameState gameState = gameStateRepository.getByGameId(gameId);
        final List<RoundResult> rounds = roundService.getRoundsForGame(gameId);

        return new GameHistory(gameState, rounds);
    }

    public List<GameHistory> getGamesHistoryForPlayer(UUID playerId) {
        final List<GameState> gameStates = gameStateRepository.getAllByPlayerId(playerId);

        final List<GameHistory> gamesHistoryForPlayer = new ArrayList<>();
        for (GameState gameState : gameStates) {
            final List<RoundResult> rounds = roundService.getRoundsForGame(gameState.getGameId());
            gamesHistoryForPlayer.add(new GameHistory(gameState, rounds));
        }

        return gamesHistoryForPlayer;
    }

    private void validateGameStart(Player player) {
        if (player == null) {
            throw new IllegalStateException("could not start new game. Player is null");
        }
    }

    private void validatePlayRound(GameType gameType, UUID gameId, UUID playerId) {
        final CashDeductionPolicy deductionPolicy = cashDeductionPoliciesMap.get(gameType);
        if (deductionPolicy == null) {
            throw new IllegalStateException(String.format("could not start a new game. Cash deduction policy not found for game type: %s", gameType));
        }
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
