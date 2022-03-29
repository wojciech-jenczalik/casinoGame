package pl.jenczalik.casinogame.domain.ports;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Clock;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.jenczalik.casinogame.config.CashPolicyConfig;
import pl.jenczalik.casinogame.config.RoundRewardsConfig;
import pl.jenczalik.casinogame.domain.model.FreeCashDeductionPolicy;
import pl.jenczalik.casinogame.domain.model.GameStartDetails;
import pl.jenczalik.casinogame.domain.model.GameState;
import pl.jenczalik.casinogame.domain.model.GameType;
import pl.jenczalik.casinogame.domain.model.PaidCashDeductionPolicy;
import pl.jenczalik.casinogame.domain.model.PlayRoundDetails;
import pl.jenczalik.casinogame.domain.model.Player;

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
    void given_a_player_with_id_and_free_game_type_when_game_started_then_should_have_5000_credits() {
        // given
        final UUID uuid = UUID.randomUUID();
        final GameStartDetails gameStartDetails = new GameStartDetails(
                GameType.FREE,
                Player.fromId(uuid)
        );

        // when
        final GameState gameState = gameService.startGame(gameStartDetails);

        // then
        assertEquals(uuid, gameState.getPlayer().getId());
        assertEquals(BigDecimal.valueOf(5000d), gameState.getBalance());
    }

    @Test
    void given_a_null_player_when_game_started_then_throw_illegal_state_exception() {
        // given
        final GameStartDetails gameStartDetails = new GameStartDetails(GameType.FREE, null);

        // when
        final IllegalStateException e = assertThrows(IllegalStateException.class, () -> gameService.startGame(gameStartDetails));
        assertEquals("could not start new game. Player is null", e.getMessage());
    }

    @Test
    void given_free_game_small_win_and_free_round_rolls_when_round_played_then_have_extra_round_and_win_cash() {
        // given
        final Player player = Player.newPlayer();
        final GameStartDetails gameStartDetails = new GameStartDetails(GameType.FREE, player);
        final GameState gameStateAfterStart = gameService.startGame(gameStartDetails);
        final PlayRoundDetails playRoundDetails = new PlayRoundDetails(gameStateAfterStart.getGameId(), player.getId(), BigDecimal.TEN);
        when(gameStateRepository.getByGameIdAndPlayerId(gameStateAfterStart.getGameId(), player.getId()))
                .thenReturn(Optional.of(gameStateAfterStart));
        when(secureRandomMock.nextInt(100)).thenReturn(SMALL_WIN_ROLL, FREE_ROUND_ROLL);

        // when
        final GameState gameStateAfterFirstRound = gameService.playRound(playRoundDetails);

        // then
        assertEquals(1, gameStateAfterFirstRound.getFreeRounds());
        // Free game mode, so no deduction. Then small win with bet of 10, resulting in a win of 30.
        assertEquals(0, BigDecimal.valueOf(5030).compareTo(gameStateAfterFirstRound.getBalance()));
    }

    @Test
    void given_paid_game_big_win_and_no_free_round_rolls_when_round_played_then_have_no_extra_round_and_win_cash() {
        // given
        final Player player = Player.newPlayer();
        final GameStartDetails gameStartDetails = new GameStartDetails(GameType.PAID, player);
        final GameState gameStateAfterStart = gameService.startGame(gameStartDetails);
        final PlayRoundDetails playRoundDetails = new PlayRoundDetails(gameStateAfterStart.getGameId(), player.getId(), BigDecimal.TEN);
        when(gameStateRepository.getByGameIdAndPlayerId(gameStateAfterStart.getGameId(), player.getId()))
                .thenReturn(Optional.of(gameStateAfterStart));
        when(secureRandomMock.nextInt(100)).thenReturn(BIG_WIN_ROLL, NO_FREE_ROUND_ROLL);

        // when
        final GameState gameStateAfterFirstRound = gameService.playRound(playRoundDetails);

        // then
        assertEquals(0, gameStateAfterFirstRound.getFreeRounds());
        // Paid game mode, so deduction of 10 (bet size). Then big win, resulting in a win of 500.
        assertEquals(0, BigDecimal.valueOf(5490).compareTo(gameStateAfterFirstRound.getBalance()));
    }

    @Test
    void given_paid_game_no_win_and_free_round_and_big_win_and_no_free_round_rolls_when_rounds_played_then_have_no_extra_round_and_win_cash() {
        // given
        final Player player = Player.newPlayer();
        final GameStartDetails gameStartDetails = new GameStartDetails(GameType.PAID, player);
        final GameState gameState = gameService.startGame(gameStartDetails);
        final PlayRoundDetails playRoundDetails = new PlayRoundDetails(gameState.getGameId(), player.getId(), BigDecimal.TEN);
        when(gameStateRepository.getByGameIdAndPlayerId(gameState.getGameId(), player.getId()))
                .thenReturn(Optional.of(gameState));
        when(secureRandomMock.nextInt(100)).thenReturn(NO_WIN_ROLL, FREE_ROUND_ROLL, BIG_WIN_ROLL, NO_FREE_ROUND_ROLL);

        // when
        final GameState gameStateAfterFirstRound = gameService.playRound(playRoundDetails);

        // then
        assertEquals(1, gameStateAfterFirstRound.getFreeRounds());
        // Paid game mode, so deduction of 10 (bet size). Then no win, resulting in a total win of 0.
        assertEquals(0, BigDecimal.valueOf(4990).compareTo(gameStateAfterFirstRound.getBalance()));

        // and when
        final GameState gameStateAfterSecondRound = gameService.playRound(playRoundDetails);

        // then
        assertEquals(0, gameStateAfterSecondRound.getFreeRounds());
        // Playing extra free round, so no deduction. Then big win, resulting in a win of 500.
        assertEquals(0, BigDecimal.valueOf(5490).compareTo(gameStateAfterSecondRound.getBalance()));
    }
}
