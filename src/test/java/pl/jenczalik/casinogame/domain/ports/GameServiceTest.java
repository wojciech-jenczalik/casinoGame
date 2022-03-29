package pl.jenczalik.casinogame.domain.ports;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import pl.jenczalik.casinogame.config.CashPolicyConfig;
import pl.jenczalik.casinogame.config.RoundRewardsConfig;
import pl.jenczalik.casinogame.domain.model.FreeCashDeductionPolicy;
import pl.jenczalik.casinogame.domain.model.GameStartDetails;
import pl.jenczalik.casinogame.domain.model.GameState;
import pl.jenczalik.casinogame.domain.model.GameType;
import pl.jenczalik.casinogame.domain.model.Player;

class GameServiceTest {
    @Mock
    private GameStateRepository gameStateRepository;

    // Subject
    private final GameService gameService = new GameService(
            new CashPolicyConfig(),
            Collections.singletonList(FreeCashDeductionPolicy.create()),
            gameStateRepository,
            new RoundService(new RoundRewardsConfig())
    );

    @Test
    void given_a_player_with_id_and_free_game_type_when_game_started_then_should_have_5000_credits() {
        // given
        final UUID uuid = UUID.randomUUID();
        final GameStartDetails gameStartDetails = new GameStartDetails(
                GameType.FREE,
                Player.fromUuid(uuid)
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
        final GameStartDetails gameStartDetails = new GameStartDetails(
                GameType.FREE,
                null
        );

        // when
        final IllegalStateException e = assertThrows(IllegalStateException.class, () -> gameService.startGame(gameStartDetails));
        assertEquals("could not start new game. Player is null", e.getMessage());
    }
}
