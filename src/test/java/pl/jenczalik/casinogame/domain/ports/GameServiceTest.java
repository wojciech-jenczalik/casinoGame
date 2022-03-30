package pl.jenczalik.casinogame.domain.ports;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Clock;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.jenczalik.casinogame.config.CashPolicyConfig;
import pl.jenczalik.casinogame.config.RoundRewardsConfig;
import pl.jenczalik.casinogame.domain.model.GameState;
import pl.jenczalik.casinogame.domain.model.GameType;
import pl.jenczalik.casinogame.domain.model.Player;
import pl.jenczalik.casinogame.domain.services.FreeCashDeductionPolicy;
import pl.jenczalik.casinogame.domain.services.PaidCashDeductionPolicy;
import pl.jenczalik.casinogame.domain.services.RoundService;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {
    private static final int NO_WIN_ROLL = 40;
    private static final int SMALL_WIN_ROLL = 5;
    private static final int BIG_WIN_ROLL = 25;
    private static final int FREE_ROUND_ROLL = 5;
    private static final int NO_FREE_ROUND_ROLL = 15;

    private final CashPolicyConfig cashPolicyConfig = new CashPolicyConfig();
    private final Clock clock = Clock.systemDefaultZone();

    @Mock
    private GameStateRepository gameStateRepository;
    @Mock
    private RoundResultRepository roundResultRepository;
    @Mock
    private SecureRandom secureRandomMock;

    // Subject
    private GameService gameService;

    @BeforeEach
    void setup() {
        Mockito.lenient().when(gameStateRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());
        Mockito.lenient().when(roundResultRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());
        gameService = new GameService(
                cashPolicyConfig,
                Arrays.asList(FreeCashDeductionPolicy.create(), PaidCashDeductionPolicy.create()),
                gameStateRepository,
                new RoundService(new RoundRewardsConfig(), secureRandomMock, roundResultRepository, clock)
        );
    }

    @Test
    void given_a_null_player_when_game_started_then_throw_illegal_state_exception() {
        // given, when, then
        final IllegalStateException e = assertThrows(IllegalStateException.class, () -> gameService.startGame(GameType.FREE, null));
        assertEquals("could not start new game. Player is null", e.getMessage());
    }

    @Test
    void given_free_game_small_win_and_free_round_rolls_when_round_played_then_have_extra_round_and_win_cash() {
        // given
        final Player player = Player.newPlayer();
        final GameState gameStateAfterStart = gameService.startGame(GameType.FREE, player);
        when(gameStateRepository.getByGameIdAndPlayerId(gameStateAfterStart.getGameId(), player.getId())).thenReturn(gameStateAfterStart);
        when(secureRandomMock.nextInt(100)).thenReturn(SMALL_WIN_ROLL, FREE_ROUND_ROLL);

        // when
        final GameState gameStateAfterFirstRound = gameService.playRound(gameStateAfterStart.getGameId(), player.getId(), BigDecimal.TEN);

        // then
        assertEquals(1, gameStateAfterFirstRound.getFreeRounds());
        // Free game mode, so no deduction. Then small win with bet of 10, resulting in a win of 30.
        assertEquals(0, BigDecimal.valueOf(5030).compareTo(gameStateAfterFirstRound.getBalance()));
    }

    @Test
    void given_paid_game_big_win_and_no_free_round_rolls_when_round_played_then_have_no_extra_round_and_win_cash() {
        // given
        final Player player = Player.newPlayer();
        final GameState gameStateAfterStart = gameService.startGame(GameType.PAID, player);
        when(gameStateRepository.getByGameIdAndPlayerId(gameStateAfterStart.getGameId(), player.getId())).thenReturn(gameStateAfterStart);
        when(secureRandomMock.nextInt(100)).thenReturn(BIG_WIN_ROLL, NO_FREE_ROUND_ROLL);

        // when
        final GameState gameStateAfterFirstRound = gameService.playRound(gameStateAfterStart.getGameId(), player.getId(), BigDecimal.TEN);

        // then
        assertEquals(0, gameStateAfterFirstRound.getFreeRounds());
        // Paid game mode, so deduction of 10 (bet size). Then big win, resulting in a win of 500.
        assertEquals(0, BigDecimal.valueOf(5490).compareTo(gameStateAfterFirstRound.getBalance()));
    }

    @Test
    void given_paid_game_no_win_and_free_round_and_big_win_and_no_free_round_rolls_when_rounds_played_then_have_no_extra_round_and_win_cash() {
        // given
        final Player player = Player.newPlayer();
        final GameState gameState = gameService.startGame(GameType.PAID, player);
        when(gameStateRepository.getByGameIdAndPlayerId(gameState.getGameId(), player.getId())).thenReturn(gameState);
        when(secureRandomMock.nextInt(100)).thenReturn(NO_WIN_ROLL, FREE_ROUND_ROLL, BIG_WIN_ROLL, NO_FREE_ROUND_ROLL);

        // when
        final GameState gameStateAfterFirstRound = gameService.playRound(gameState.getGameId(), player.getId(), BigDecimal.TEN);

        // then
        assertEquals(1, gameStateAfterFirstRound.getFreeRounds());
        // Paid game mode, so deduction of 10 (bet size). Then no win, resulting in a total win of 0.
        assertEquals(0, BigDecimal.valueOf(4990).compareTo(gameStateAfterFirstRound.getBalance()));

        // and when
        final GameState gameStateAfterSecondRound = gameService.playRound(gameState.getGameId(), player.getId(), BigDecimal.TEN);

        // then
        assertEquals(0, gameStateAfterSecondRound.getFreeRounds());
        // Playing extra free round, so no deduction. Then big win, resulting in a win of 500.
        assertEquals(0, BigDecimal.valueOf(5490).compareTo(gameStateAfterSecondRound.getBalance()));
    }

    @Test
    void given_negative_bet_then_should_throw_illegal_argument_exception() {
        // given
        final Player player = Player.newPlayer();
        final GameState gameState = gameService.startGame(GameType.PAID, player);
        when(gameStateRepository.getByGameIdAndPlayerId(gameState.getGameId(), player.getId())).thenReturn(gameState);

        // when, then
        final IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> gameService.playRound(gameState.getGameId(), player.getId(), BigDecimal.valueOf(-1)));

        assertEquals(e.getMessage(), "bet (-1) can not be a negative amount");
    }

    @Test
    void given_bet_larger_than_configured_limit_then_should_throw_illegal_argument_exception() {
        // given
        final Player player = Player.newPlayer();
        final GameState gameState = gameService.startGame(GameType.PAID, player);
        when(gameStateRepository.getByGameIdAndPlayerId(gameState.getGameId(), player.getId())).thenReturn(gameState);

        // when, then
        final IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> gameService.playRound(gameState.getGameId(), player.getId(), BigDecimal.valueOf(15)));

        assertEquals(e.getMessage(), "bet (15) can not be more than 10.0");
    }

    @Test
    void given_bet_larger_than_balance_then_should_throw_illegal_argument_exception() {
        // given
        final Player player = Player.newPlayer();
        final GameState gameState = gameService.startGame(GameType.PAID, player);
        gameState.deductBetFromBalance(new PaidCashDeductionPolicy(), BigDecimal.valueOf(4995));
        when(gameStateRepository.getByGameIdAndPlayerId(gameState.getGameId(), player.getId())).thenReturn(gameState);

        // when, then
        final IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> gameService.playRound(gameState.getGameId(), player.getId(), BigDecimal.valueOf(10)));

        assertEquals(e.getMessage(), "bet (10) can not be greater than current balance (5.0)");
    }
}
